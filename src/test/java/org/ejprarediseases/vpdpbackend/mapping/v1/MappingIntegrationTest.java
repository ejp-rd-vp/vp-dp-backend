package org.ejprarediseases.vpdpbackend.mapping.v1;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest
@ActiveProfiles("test")
@Tag("IntegrationTest")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {MappingService.class, MappingController.class})
@DisplayName("Mapping Integration Tests")
public class MappingIntegrationTest {

    @Autowired
    private MappingService mappingService;

    @Autowired
    WebTestClient webTestClient;

    @Test
    @WithMockUser
    public void shouldGetGeneMapping() {
        GeneMapping geneMapping = mappingService.getGeneMapping("9008");
        assert (geneMapping.getHgncId().equals("9008"));
        assert (geneMapping.getOrphaCodes().size() == 2);
        assert (geneMapping.getOrphaCodes().contains("88924"));
        assert (geneMapping.getOrphaCodes().contains("730"));
    }
}
