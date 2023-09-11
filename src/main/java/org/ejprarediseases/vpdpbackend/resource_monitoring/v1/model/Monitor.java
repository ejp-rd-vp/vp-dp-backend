package org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

import java.sql.Timestamp;

@Data
@Entity
@DynamicInsert
@Table(name = "resource_monitor")
public class Monitor {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Basic
    @Column(name = "resource_id")
    private String resourceId;
    @Basic
    @Column(name = "response_status_code")
    private int responseStatusCode;
    @Basic
    @Column(name = "response_time")
    private long responseTime;
    @Basic
    @Column(name = "response_body", columnDefinition="text", length=10485760)
    private String responseBody;
    @Basic
    @Column(name = "timestamp")
    private Timestamp timestamp;
}
