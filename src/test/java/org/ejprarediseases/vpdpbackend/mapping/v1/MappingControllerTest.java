package org.ejprarediseases.vpdpbackend.mapping.v1;

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
import reactor.core.publisher.Mono;


@WebFluxTest(MappingController.class)
@ActiveProfiles("test")
@Tag("UnitTest")
@DisplayName("Mapping Controller Unit Tests")
public class MappingControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    MappingService mappingService;

    @Test
    @WithMockUser
    public void shouldGetGeneMapping() {
        Mono<GeneMapping> geneMappingMono = Mono.just(new GeneMapping());
        Mockito.when(mappingService.getGeneMapping("7"))
                .thenReturn(geneMappingMono.block());
        webTestClient.get().uri(uriBuilder ->
                        uriBuilder
                                .path("/v1/mapping/gene/7")
                                .build())
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody(GeneMapping.class);
    }

}
