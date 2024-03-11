package org.ejprarediseases.vpdpbackend.notification.v1;

import org.ejprarediseases.vpdpbackend.notification.v1.model.Notification;
import org.ejprarediseases.vpdpbackend.notification.v1.model.enums.NotificationStatus;
import org.ejprarediseases.vpdpbackend.notification.v1.model.enums.NotificationType;
import org.ejprarediseases.vpdpbackend.notification.v1.service.EmailNotificationService;
import org.ejprarediseases.vpdpbackend.resource.v1.model.Resource;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;

@ActiveProfiles("test")
@Tag("UnitTest")
@ExtendWith({MockitoExtension.class})
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Email Notification Service Unit Tests")
public class EmailNotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private EmailNotificationService emailNotificationService;

    @Test
    public void shouldSendEmailNotification() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        Resource resource = new Resource();
        resource.setId("id");
        resource.setResourceName("test resource");
        resource.setEmail("someemail@domain.com");
        resource.setCreated(currentTimestamp);
        Notification notification =
                emailNotificationService.sendEmailNotification(resource, NotificationType.PERFORMANCE_ISSUE);
        //TODO test case email was sent
        assert(notification.getStatus().equals(NotificationStatus.FAILED));
    }

}
