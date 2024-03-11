package org.ejprarediseases.vpdpbackend.notification.v1;

import org.ejprarediseases.vpdpbackend.db.DatabaseSetupExtension;
import org.ejprarediseases.vpdpbackend.notification.v1.model.Notification;
import org.ejprarediseases.vpdpbackend.notification.v1.model.enums.NotificationChannel;
import org.ejprarediseases.vpdpbackend.notification.v1.model.enums.NotificationStatus;
import org.ejprarediseases.vpdpbackend.notification.v1.model.enums.NotificationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Tag("UnitTest")
@ExtendWith(DatabaseSetupExtension.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Notification Repository Unit Tests")
public class NotificationRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    NotificationRepository repository;

    @Test
    public void shouldFindNoNotificationsIfRepositoryIsEmpty() {
        Iterable<Notification> notifications = repository.findAll();
        assertThat(notifications).isEmpty();
    }

    @Test
    public void shouldFindAllByResourceIdEqualsAndTimestampBetween() {
        Notification notification = new Notification();
        notification.setResourceId("2");
        notification.setStatus(NotificationStatus.SENT);
        notification.setType(NotificationType.PERFORMANCE_ISSUE);
        notification.setContent("TEST");
        notification.setChannel(NotificationChannel.EMAIL);
        notification.setTimestamp(new Timestamp(System.currentTimeMillis()));
        entityManager.persist(notification);
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        Timestamp startTimestamp = Timestamp.valueOf(ZonedDateTime.now()
                .minus(2, ChronoUnit.DAYS).toLocalDateTime());
        Iterable<Notification> notifications =
                repository.findAllByResourceIdEqualsAndTimestampBetween("2", startTimestamp, currentTimestamp);
        assertThat(notifications).hasSize(1).contains(notification);
    }
}
