package org.ejprarediseases.vpdpbackend.mapping.v1;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.Mockito.mock;

@WebFluxTest
@ActiveProfiles("test")
@Tag("UnitTest")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {MappingService.class})
@DisplayName("Mapping Service Unit Tests")
public class MappingServiceTest {

    private MappingService mappingService;
    private WebClient webClient;

    @BeforeEach
    void setUp() {
        webClient = mock(WebClient.class);
        WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);
        mappingService = new MappingService();
    }

}
