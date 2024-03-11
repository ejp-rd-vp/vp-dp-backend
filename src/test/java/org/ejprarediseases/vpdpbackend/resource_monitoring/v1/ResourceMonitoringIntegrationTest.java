package org.ejprarediseases.vpdpbackend.resource_monitoring.v1;

import org.ejprarediseases.vpdpbackend.db.DatabaseSetupExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(DatabaseSetupExtension.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@Tag("IntegrationTest")
@DisplayName("Resource Monitoring Integration Tests")
public class ResourceMonitoringIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void shouldGetResourceMonitoring() {
        webTestClient.get().uri(uriBuilder ->
                        uriBuilder
                                .path("/v1/monitoring")
                                .queryParam("resourceId", "1")
                                .queryParam("periodInDays", 1)
                                .build())
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody();
    }
}

