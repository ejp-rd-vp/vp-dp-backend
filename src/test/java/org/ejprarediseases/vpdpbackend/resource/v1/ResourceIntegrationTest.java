package org.ejprarediseases.vpdpbackend.resource.v1;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest()
@ContextConfiguration(classes = {ResourceService.class, ResourceController.class})
@ActiveProfiles("test")
@Tag("IntegrationTest")
@DisplayName("Resource Integration Tests")
public class ResourceIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    @SneakyThrows
    @Test
    @WithMockUser
    public void shouldGetResources() {
        webTestClient.get().uri(uriBuilder ->
                        uriBuilder
                                .path("/v1/resources")
                                .build())
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody();
    }
}
