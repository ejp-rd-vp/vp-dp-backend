package org.ejprarediseases.vpdpbackend.notification.v1.service;

import lombok.RequiredArgsConstructor;
import org.ejprarediseases.vpdpbackend.notification.v1.model.Notification;
import org.ejprarediseases.vpdpbackend.notification.v1.NotificationRepository;
import org.ejprarediseases.vpdpbackend.resource.v1.ResourceService;
import org.ejprarediseases.vpdpbackend.resource.v1.model.Resource;
import org.ejprarediseases.vpdpbackend.resource_monitoring.v1.ResourceMonitoringService;
import org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model.Period;
import org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model.ResourceMonitoringSummary;
import org.ejprarediseases.vpdpbackend.utils.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.*;
import static org.ejprarediseases.vpdpbackend.notification.v1.model.enums.NotificationType.FUNCTIONAL_ISSUE;

@Service
@RequiredArgsConstructor
public class NotificationRequirementService {

    private final ResourceService resourceService;
    private final ResourceMonitoringService monitoringService;
    private final EmailNotificationService emailNotificationService;
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;
    static Logger logger = LoggerFactory.getLogger(StringConverter.class);

    /**
     * Scheduled method to check notification requirements for resources.
     *
     * This method is scheduled to run periodically. It retrieves a list of queryable resources and
     * evaluates their monitoring summaries against predefined criteria. If the criteria are not met and
     * no notifications have been sent within a specified period, an email notification for functional
     * issues is triggered.
     */
    @Scheduled(timeUnit = TimeUnit.HOURS, initialDelay = 1, fixedRate = 1)
    public void checkNotificationRequirements() {
        try {
            List<Resource> resources = resourceService.getAllQueryableResources();
            for (Resource resource : resources) {
                ResourceMonitoringSummary summary = monitoringService
                        .getResourceMonitoringSummary(resource.getId(), notificationService.DEFAULT_PERIOD);
                if(resourceMinNumberOfTestsInPeriod(2, summary)
                        && minAcceptedTestResponseCodes(70, summary)) {
                    continue;
                } else {
                    if(noNotificationSentInPeriod(new Period(2, DAYS), resource)) {
                        emailNotificationService.sendEmailNotification(resource, FUNCTIONAL_ISSUE);
                    }
                }
            }
        } catch (IOException io) {
            logger.error("Failed to load list of resources with cause: " + io.getMessage());
        }
    }

    /**
     * Checks if the number of tests for a resource within a monitoring period is above a given minimum.
     *
     * @param minNumberOfTests The minimum number of tests required.
     * @param summary The resource monitoring summary to check against.
     * @return True if the number of tests is greater than or equal to the minimum, false otherwise.
     */
    private boolean resourceMinNumberOfTestsInPeriod(int minNumberOfTests, ResourceMonitoringSummary summary) {
        return summary.getNumberOfTests() >= minNumberOfTests;
    }

    /**
     * Checks if the percentage of accepted test response codes is above a given threshold.
     *
     * @param minAcceptedCodesInPercent The minimum percentage of accepted response codes.
     * @param summary The resource monitoring summary to check against.
     * @return True if the percentage of accepted response codes is above the threshold, false otherwise.
     */
    private boolean minAcceptedTestResponseCodes(int minAcceptedCodesInPercent, ResourceMonitoringSummary summary) {
        int acceptedStatusCodes =
                summary.getNumberOfInformationalResponses() + summary.getNumberOfSuccessfulResponses();
        int unAcceptedStatusCodes = summary.getNumberOfClientErrorResponses() +
                summary.getNumberOfServerErrorResponses() +
                summary.getNumberOfRedirectionResponses();
        return minAcceptedCodesInPercent <=
                ((acceptedStatusCodes / (unAcceptedStatusCodes + acceptedStatusCodes)) * 100);
    }

    /**
     * Checks if any notifications have been sent for a resource within a specified period.
     *
     * @param period The time period to consider for checking notifications.
     * @param resource The resource to check notifications for.
     * @return True if no notifications have been sent within the period, false otherwise.
     */
    private boolean noNotificationSentInPeriod(Period period, Resource resource) {
        Timestamp startTimestamp = Timestamp.valueOf(ZonedDateTime.now(
                ZoneId.of("Europe/Berlin")).minus(period.getValue(), period.getUnit()).toLocalDateTime());
        Timestamp endTimestamp =
                Timestamp.valueOf(ZonedDateTime.now(ZoneId.of("Europe/Berlin")).toLocalDateTime());
        List<Notification> notifications = notificationRepository
                .findAllByResourceIdEqualsAndTimestampBetween(resource.getId(), startTimestamp, endTimestamp);
        return notifications.size() == 0;
    }


}
