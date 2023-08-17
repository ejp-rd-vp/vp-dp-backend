package org.ejprarediseases.vpdpbackend.notification.v1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ejprarediseases.vpdpbackend.notification.v1.model.enums.NotificationChannel;
import org.ejprarediseases.vpdpbackend.notification.v1.model.enums.NotificationStatus;
import org.ejprarediseases.vpdpbackend.notification.v1.model.enums.NotificationType;
import org.hibernate.annotations.DynamicInsert;

import java.sql.Timestamp;

@Data
@Entity
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification")
public class Notification {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Basic
    @Column(name = "channel")
    @Enumerated(EnumType.STRING)
    private NotificationChannel channel;
    @Basic
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;
    @Basic
    @Column(name = "type")
    private NotificationType type;
    @Basic
    @Column(name = "resource_id")
    private String resourceId;
    @Basic
    @Column(name = "content")
    private String content;
    @Basic
    @Column(name = "timestamp")
    private Timestamp timestamp;
}
