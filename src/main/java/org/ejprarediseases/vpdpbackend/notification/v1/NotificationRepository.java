package org.ejprarediseases.vpdpbackend.notification.v1;

import org.ejprarediseases.vpdpbackend.notification.v1.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByResourceIdEqualsAndTimestampBetween(
            String resourceId, Timestamp startTimestamp, Timestamp endTimestamp);
}
