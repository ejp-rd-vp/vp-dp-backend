package org.ejprarediseases.vpdpbackend.resource.v1;

import lombok.SneakyThrows;
import org.ejprarediseases.vpdpbackend.resource.v1.model.Resource;
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

import java.util.ArrayList;
import java.util.List;

@WebFluxTest(ResourceController.class)
@ActiveProfiles("test")
@Tag("UnitTest")
@DisplayName("Resource Controller Unit Tests")
public class ResourceControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    private ResourceService resourceService;

    @SneakyThrows
    @Test
    @WithMockUser
    public void shouldGetResources() {
        List<Resource> resources = new ArrayList<>();
        Resource resource = new Resource();
        resource.setResourceName("first resource");
        resource.setEmail("someemail@domain.com");
        resources.add(resource);
        Mockito.when(resourceService.getAllResources()).thenReturn(resources);
        webTestClient.get().uri(uriBuilder ->
                        uriBuilder
                                .path("/v1/resources")
                                .build())
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody().equals(resources);
    }

}
