package org.ejprarediseases.vpdpbackend.notification.v1;

import org.ejprarediseases.vpdpbackend.db.DatabaseSetupExtension;
import org.ejprarediseases.vpdpbackend.notification.v1.model.enums.NotificationType;
import org.ejprarediseases.vpdpbackend.notification.v1.service.EmailNotificationService;
import org.ejprarediseases.vpdpbackend.resource.v1.model.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(DatabaseSetupExtension.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@Tag("IntegrationTest")
@DisplayName("Notification Integration Tests")
public class NotificationIntegrationTest {

    @Autowired
    private NotificationRepository repository;

    @Autowired
    private EmailNotificationService emailNotificationService;


    @Test
    public void shouldSendEmailNotification() {
        Resource resource = new Resource();
        resource.setEmail("someemail@domain.com");
        resource.setResourceName("RESOURCE NAME");
        emailNotificationService.sendEmailNotification(resource, NotificationType.PERFORMANCE_ISSUE);
        assertThat(repository.findAll()).hasSize(1);
    }
}
