package org.ejprarediseases.vpdpbackend.notification.v1.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.ejprarediseases.vpdpbackend.notification.v1.*;
import org.ejprarediseases.vpdpbackend.notification.v1.handler.HtmlHandler;
import org.ejprarediseases.vpdpbackend.notification.v1.model.Notification;
import org.ejprarediseases.vpdpbackend.notification.v1.model.enums.NotificationChannel;
import org.ejprarediseases.vpdpbackend.notification.v1.model.enums.NotificationStatus;
import org.ejprarediseases.vpdpbackend.notification.v1.model.enums.NotificationType;
import org.ejprarediseases.vpdpbackend.resource.v1.model.Resource;
import org.ejprarediseases.vpdpbackend.resource_monitoring.v1.ResourceMonitoringService;
import org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model.Monitor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.*;

@Service
@Validated
@RequiredArgsConstructor
public class EmailNotificationService {

    @Value("${application.notification.senderEmailAddress}")
    private String senderEmailAddress;

    @Value("${application.notification.senderEmailPassword}")
    private String senderEmailPassword;
    private final NotificationRepository notificationRepository;

    private final ResourceMonitoringService monitoringService;

    private final NotificationService notificationService;

    /**
     * Sends an email notification to the specified resource using the given notification type.
     * The method builds an email message and attempts to send it. If successful, a notification
     * is created and saved with a "SENT" status. If the sending process fails, a notification
     * with a "FAILED" status is created, including an error message describing the cause of failure.
     *
     * @param resource The resource for which the notification is being sent.
     * @param type     The type of the notification.
     */
    public Notification sendEmailNotification(@Valid Resource resource, NotificationType type) {
        Notification notification = new Notification();
        try {
            Message message = buildEmailMessage(resource, type);
            Transport.send(message);
            notification.setStatus(NotificationStatus.SENT);
        } catch (Exception e) {
            notification.setStatus(NotificationStatus.FAILED);
            notification.setContent("sendEmailNotification failed with cause: " + e.getMessage());
        } finally {
            notification.setChannel(NotificationChannel.EMAIL);
            notification.setType(type);
            notification.setResourceId(resource.getId());
            notificationRepository.save(notification);
        }
        return notification;
    }

    /**
     * Builds and returns an email message based on the recipient's email address and notification type.
     *
     * @param resource The recipient's resource information.
     * @param type                   The type of notification.
     * @return The built email message.
     * @throws MessagingException If there is an issue with building the email message.
     * @throws IOException        If an I/O error occurs while reading templates.
     */
    private Message buildEmailMessage(
            Resource resource, NotificationType type)
            throws MessagingException, IOException {
        Session session = getNewEmailSession();
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmailAddress));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(resource.getEmail()));
        message.setSubject(getEmailSubject(type));
        MimeBodyPart htmlPart = new MimeBodyPart();
        String content = fillOutTemplateBasedOnNotificationType(type, getTemplateBasedOnType(type), resource) ;
        htmlPart.setContent(content, "text/html");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(htmlPart);
        multipart.addBodyPart(buildEmailAttachment(
                "src/main/resources/static/logo/ejp.png", "<ejpLogo>"));
        message.setContent(multipart);
        return message;
    }

    /**
     * Fill out an HTML template based on the notification type, using information from a resource.
     *
     * This method takes a notification type, an HTML template, and a resource, and populates the template
     * with relevant information based on the notification type and resource details. The method handles
     * different notification types, such as UPTIME_ISSUE, PERFORMANCE_ISSUE, SECURITY_ISSUE, SCALING_ISSUE,
     * and FUNCTIONAL_ISSUE. For FUNCTIONAL_ISSUE notifications, it extracts and processes information from
     * monitors to generate a table of failed tests.
     *
     * @param type The type of notification for which the template is being filled out.
     * @param template The HTML template to be populated with information.
     * @param resource The resource associated with the notification.
     * @return A string containing the populated HTML template with appropriate values.
     */
    private String fillOutTemplateBasedOnNotificationType(
            NotificationType type, String template, Resource resource) {
        switch (type) {
            case UPTIME_ISSUE:
            case PERFORMANCE_ISSUE:
            case SECURITY_ISSUE:
            case  SCALING_ISSUE:
                return template;
            case FUNCTIONAL_ISSUE:
                List<Monitor> monitors = monitoringService.getMonitorsByResourceId(
                        resource.getId(), notificationService.DEFAULT_PERIOD);
                List<Monitor> failedMonitors = monitoringService.filterMonitorsAccordingToHttpCategories(
                        monitors, notificationService.UNACCEPTED_HTTP_CATEGORIES);
                List<String> failedTests = failedMonitors.stream().map(monitor ->
                        HtmlHandler.listToTableRow(
                                Arrays.asList(String.valueOf(monitor.getResponseStatusCode()),
                                        monitor.getResponseTime() + " ms",
                                        monitor.getTimestamp() + " CET"))).toList();
                Map<String, String> variables = Map.ofEntries(
                        Map.entry("[RECIPIENT]", resource.getResourceName()),
                        Map.entry("[RESOURCE_ID]", resource.getId()),
                        Map.entry("[RESOURCE_NAME]", resource.getResourceName()),
                        Map.entry("[RESOURCE_URL]", resource.getResourceAddress()),
                        Map.entry("[FAILED_TESTS]", String.join("", failedTests))
                );
                String[] keys = variables.keySet().toArray(new String[4]);
                String[] values = variables.values().toArray(new String[4]);
                return StringUtils.replaceEach(template, keys, values);
            default:
                return template;
        }
    }

    /**
     * Returns the email template content based on the notification type.
     *
     * @param type The type of notification.
     * @return The email template content.
     * @throws IOException If an I/O error occurs while reading templates.
     */
    private String getTemplateBasedOnType(NotificationType type) throws IOException {
        return switch (type) {
            case UPTIME_ISSUE, PERFORMANCE_ISSUE, SECURITY_ISSUE, FUNCTIONAL_ISSUE, SCALING_ISSUE ->
                    getEmailContentAsString(
                            "src/main/resources/templates/notification_template/resourceFunctionalIssue.html");
        };
    }

    /**
     * Builds and returns a MIME body part for an email attachment.
     *
     * @param templatePathFromContentRoot The path to the attachment template from the content root.
     * @param bodyPartId                 The ID for the body part.
     * @return The MIME body part for the attachment.
     * @throws MessagingException If there is an issue with building the body part.
     * @throws IOException        If an I/O error occurs while reading the attachment template.
     */
    private MimeBodyPart buildEmailAttachment(
            String templatePathFromContentRoot, String bodyPartId) throws MessagingException, IOException {
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.attachFile(templatePathFromContentRoot);
        bodyPart.setContentID(bodyPartId);
        return bodyPart;
    }

    /**
     * Creates and returns a new email session for sending emails.
     *
     * @return The new email session.
     */
    private Session getNewEmailSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "outlook.office365.com");
        props.put("mail.smtp.port", "587");
        return Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmailAddress, senderEmailPassword);
                    }
                });
    }

    /**
     * Reads and returns the content of an email template as a string.
     *
     * @param fileName The path to the email template.
     * @return The content of the email template as a string.
     * @throws IOException If an I/O error occurs while reading the template.
     */
    private String getEmailContentAsString(String fileName) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String str;
        while ((str = in.readLine()) != null) {
            contentBuilder.append(str);
        }
        in.close();
        return contentBuilder.toString();
    }

    /**
     * Returns the email subject based on the notification type.
     *
     * @param type The type of notification.
     * @return The email subject.
     */
    private String getEmailSubject(NotificationType type) {
        return switch (type) {
            case UPTIME_ISSUE, PERFORMANCE_ISSUE, SECURITY_ISSUE, FUNCTIONAL_ISSUE, SCALING_ISSUE ->
                    "[ACTION REQUIRED] VP-PORTAL Resource Monitoring Alert";
        };
    }


}
