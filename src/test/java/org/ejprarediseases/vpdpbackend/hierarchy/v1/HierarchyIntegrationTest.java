package org.ejprarediseases.vpdpbackend.hierarchy.v1;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClient;

@WebFluxTest
@ActiveProfiles("test")
@Tag("IntegrationTest")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {HierarchyService.class, HierarchyController.class})
@DisplayName("Hierarchy Integration Tests")
public class HierarchyIntegrationTest {

    @Autowired
    private HierarchyService hierarchyService;

    private WebClient webClient;

    @BeforeEach
    public void setup() {
        webClient = WebClient.create();
    }

    @Test
    public void shouldFetchOrphaCodeHierarchyUpAsString() {
        String hierarchyAsJson = hierarchyService.getOrphaCodeHierarchyUp("730");
        assert (hierarchyAsJson.contains("apiVersion"));
        assert (hierarchyAsJson.contains("parents"));
    }

    @Test
    public void shouldFetchOrphaCodeHierarchyDownAsString() {
        String hierarchyAsJson = hierarchyService.getOrphaCodeHierarchyDown("730");
        assert (hierarchyAsJson.contains("apiVersion"));
        assert (hierarchyAsJson.contains("childs"));
    }
}
