package org.ejprarediseases.vpdpbackend.hierarchy.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.ejprarediseases.vpdpbackend.hierarchy.v1.model.Hierarchy;
import org.ejprarediseases.vpdpbackend.hierarchy.v1.model.HierarchyWay;
import org.ejprarediseases.vpdpbackend.hierarchy.v1.model.OrphaCodeHierarchyDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@WebFluxTest(HierarchyController.class)
@ActiveProfiles("test")
@Tag("UnitTest")
@DisplayName("Hierarchy Controller Unit Tests")
public class HierarchyControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    private HierarchyService hierarchyService;

    @Test
    @WithMockUser
    public void shouldGetOrphaCodeHierarchy() {
        Mono<List<OrphaCodeHierarchyDto>> orphaCodeHierarchyDtoListMono = Mono.just(new ArrayList<>());
        Mockito.when(hierarchyService.convertOrphaCodeHierarchyToListOfOrphaCodeDto(new Hierarchy()))
                .thenReturn(orphaCodeHierarchyDtoListMono.block());
        webTestClient.get().uri(uriBuilder ->
                        uriBuilder
                                .path("/v1/hierarchy")
                                .queryParam("ways", "UP")
                                .queryParam("orphaCode", "730")
                                .queryParam("numbersOfLevels", 1)
                                .build())
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody(ArrayList.class);
    }

}
