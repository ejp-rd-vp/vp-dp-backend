package org.ejprarediseases.vpdpbackend.disease.v1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@WebFluxTest
@ActiveProfiles("test")
@Tag("IntegrationTest")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {DiseaseService.class, DiseaseRepository.class, DiseaseController.class})
@DisplayName("Disease Integration Tests")
public class DiseaseIntegrationTest {

}
