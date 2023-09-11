package org.ejprarediseases.vpdpbackend.resource_monitoring.v1;

import org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model.Monitor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
@DisplayName("Resource Monitoring Repository Unit Tests")
public class ResourceMonitoringRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    ResourceMonitoringRepository repository;

    @Test
    public void shouldFindNoMonitorsIfRepositoryIsEmpty() {
        Iterable<Monitor> monitors = repository.findAll();
        assertThat(monitors).isEmpty();
    }

    @Test
    public void shouldBeAbleToAddMonitor() {
        Monitor monitor = new Monitor();
        monitor.setResourceId("1");
        repository.save(monitor);
        assertThat(repository.findAll()).hasSize(1);
    }

    @Test
    public void shouldFindAllByResourceIdEqualsAndTimestampBetween() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        Timestamp oneDaysAgo = Timestamp.valueOf(ZonedDateTime.now()
                .minus(1, ChronoUnit.DAYS).toLocalDateTime());
        Timestamp twoDaysAgo = Timestamp.valueOf(ZonedDateTime.now()
                .minus(2, ChronoUnit.DAYS).toLocalDateTime());
        Timestamp threeDaysAgo = Timestamp.valueOf(ZonedDateTime.now()
                .minus(3, ChronoUnit.DAYS).toLocalDateTime());
        Timestamp fourDaysAgo = Timestamp.valueOf(ZonedDateTime.now()
                .minus(4, ChronoUnit.DAYS).toLocalDateTime());
        Monitor monitor1 = new Monitor();
        monitor1.setResourceId("1");
        monitor1.setTimestamp(oneDaysAgo);
        entityManager.persist(monitor1);
        Monitor monitor2 = new Monitor();
        monitor2.setResourceId("1");
        monitor2.setTimestamp(threeDaysAgo);
        entityManager.persist(monitor2);
        assertThat(repository.findAllByResourceIdEqualsAndTimestampBetween(
                "1", currentTimestamp, currentTimestamp)).hasSize(0);
        assertThat(repository.findAllByResourceIdEqualsAndTimestampBetween(
                "1", twoDaysAgo, currentTimestamp)).hasSize(1);
        assertThat(repository.findAllByResourceIdEqualsAndTimestampBetween(
                "1", fourDaysAgo, currentTimestamp)).hasSize(2);
    }
}
