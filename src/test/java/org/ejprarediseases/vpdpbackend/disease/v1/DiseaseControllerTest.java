package org.ejprarediseases.vpdpbackend.disease.v1;

import org.ejprarediseases.vpdpbackend.disease.v1.model.Disease;
import org.ejprarediseases.vpdpbackend.disease.v1.model.DiseaseDto;
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

@WebFluxTest(DiseaseController.class)
@ActiveProfiles("test")
@Tag("UnitTest")
@DisplayName("Disease Controller Unit Tests")
public class DiseaseControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    private DiseaseService diseaseService;

    @Test
    @WithMockUser
    public void testGetDiseaseByOrphaCode() {
        Mono<DiseaseDto> diseaseDtoMono = Mono.just(new DiseaseDto());
        Mockito.when(diseaseService.diseaseToDto(new Disease())).thenReturn(diseaseDtoMono.block());
        webTestClient.get().uri(uriBuilder ->
                        uriBuilder
                                .path("/v1/disease")
                                .queryParam("orphaCode", "730")
                                .build())
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody(DiseaseDto.class);
    }
}
