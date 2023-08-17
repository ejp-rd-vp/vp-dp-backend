package org.ejprarediseases.vpdpbackend.resource_monitoring.v1;

import org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface ResourceMonitoringRepository extends JpaRepository<Monitor, Long> {
    List<Monitor> findAllByResourceIdEqualsAndTimestampBetween(
            String resourceId, Timestamp startTimestamp, Timestamp endTimestamp);
}
