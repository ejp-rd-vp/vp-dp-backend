package org.ejprarediseases.vpdpbackend.resource_monitoring.v1;

import org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model.Period;
import org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model.ResourceMonitoringSummary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.temporal.ChronoUnit;

@WebFluxTest(ResourceMonitoringController.class)
@ActiveProfiles("test")
@Tag("UnitTest")
@DisplayName("Resource Monitoring Controller Unit Tests")
public class ResourceMonitoringControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    private ResourceMonitoringService resourceMonitoringService;

    @Test
    @WithMockUser
    public void shouldGetMonitoringStatus() {
        Period period = new Period(1, ChronoUnit.DAYS);
        ResourceMonitoringSummary summary = new ResourceMonitoringSummary();
        summary.setResourceId("1");
        summary.setPeriod(period);
        summary.setNumberOfTests(1);
        summary.setAverageResponseTimeInMilliSeconds(99);
        Mockito.when(resourceMonitoringService.getResourceMonitoringSummary("1", period)).thenReturn(summary);
        webTestClient.get().uri(uriBuilder ->
                        uriBuilder
                                .path("/v1/monitoring")
                                .queryParam("resourceId", "1")
                                .queryParam("periodInDays", 1)
                                .build())
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody().equals(summary);
    }
}
