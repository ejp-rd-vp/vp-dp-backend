package org.ejprarediseases.vpdpbackend.resource_monitoring.v1;

import org.ejprarediseases.vpdpbackend.resource.v1.ResourceService;
import org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model.Monitor;
import org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model.Period;
import org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model.ResourceMonitoringSummary;
import org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model.enums.HttpStatusCategory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {ResourceMonitoringService.class})
@DisplayName("Resource Monitoring Service Unit Tests")
public class ResourceMonitoringServiceTest {

    @Mock
    private ResourceMonitoringRepository repository;

    @Mock
    private ResourceService resourceService;

    @InjectMocks
    private ResourceMonitoringService resourceMonitoringService;


    @Test
    public void shouldFilterMonitorsAccordingToHttpCategories() {
        Monitor monitor1 = new Monitor();
        monitor1.setResourceId("3");
        monitor1.setResponseStatusCode(200);
        Monitor monitor2 = new Monitor();
        monitor2.setResourceId("4");
        monitor2.setResponseStatusCode(404);
        List<Monitor> monitors = Arrays.asList(monitor1, monitor2);
        List<Monitor> successfulMonitors = resourceMonitoringService.filterMonitorsAccordingToHttpCategories(
                monitors, List.of(HttpStatusCategory.SUCCESSFUL_RESPONSE));
        assertThat(successfulMonitors).hasSize(1);
        assert (successfulMonitors.get(0).getResourceId().equals("3"));
        List<Monitor> clientErrorMonitors = resourceMonitoringService.filterMonitorsAccordingToHttpCategories(
                monitors, List.of(HttpStatusCategory.CLIENT_ERROR_RESPONSE));
        assertThat(clientErrorMonitors).hasSize(1);
        assert (clientErrorMonitors.get(0).getResourceId().equals("4"));
    }

    @Test
    public void shouldGetResourceMonitoringSummary() {
        Period period = new Period(1, ChronoUnit.DAYS);
        Monitor monitor1 = new Monitor();
        monitor1.setResourceId("1");
        monitor1.setResponseStatusCode(100);
        Monitor monitor2 = new Monitor();
        monitor2.setResourceId("1");
        monitor2.setResponseStatusCode(200);
        Monitor monitor3 = new Monitor();
        monitor3.setResourceId("1");
        monitor3.setResponseStatusCode(300);
        Monitor monitor4 = new Monitor();
        monitor4.setResourceId("1");
        monitor4.setResponseStatusCode(400);
        Monitor monitor5 = new Monitor();
        monitor5.setResourceId("1");
        monitor5.setResponseStatusCode(500);
        List<Monitor> monitors = Arrays.asList(monitor1, monitor2, monitor3, monitor4, monitor5);
        Mockito.doReturn(monitors).when(repository).findAllByResourceIdEqualsAndTimestampBetween(Mockito.eq("5"),
                Mockito.any(Timestamp.class), Mockito.any(Timestamp.class));
        ResourceMonitoringSummary summary =
                resourceMonitoringService.getResourceMonitoringSummary("5", period);
        assert(summary.getResourceId().equals("5"));
        assert(summary.getNumberOfSuccessfulResponses() == 1);
        assert(summary.getNumberOfClientErrorResponses() == 1);
    }
}
