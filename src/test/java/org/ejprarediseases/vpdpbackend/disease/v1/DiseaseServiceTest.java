package org.ejprarediseases.vpdpbackend.disease.v1;

import org.ejprarediseases.vpdpbackend.disease.v1.model.Disease;
import org.ejprarediseases.vpdpbackend.disease.v1.model.DiseaseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@Tag("UnitTest")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {DiseaseService.class})
@DisplayName("Disease Service Unit Tests")
public class DiseaseServiceTest {

    @Mock
    DiseaseRepository diseaseRepository;
    @InjectMocks
    DiseaseService diseaseService;

    @Test
    public void testDiseaseToDto() {
        Disease disease = new Disease();
        disease.setCodes("omimCode:220210");
        disease.setName("3C syndrome");
        disease.setSynonyms("Craniocerebellocardiac dysplasia,Ritscher-Schinzel syndrome");
        disease.setOrphaCode("7");
        DiseaseDto diseaseDto = diseaseService.diseaseToDto(disease);
        assert(diseaseDto.getCodes()).equals(disease.getCodes());
        assert(diseaseDto.getName()).equals(disease.getName());
        assert(diseaseDto.getSynonyms()).equals(disease.getSynonyms());
        assert(diseaseDto.getOrphaCode()).equals(disease.getOrphaCode());
    }

    @Test
    public void testGetDiseaseByOrphaCode() {
        Disease disease = new Disease();
        disease.setCodes("omimCode:220210");
        disease.setName("3C syndrome");
        disease.setSynonyms("Craniocerebellocardiac dysplasia,Ritscher-Schinzel syndrome");
        disease.setOrphaCode("7");
        Disease diseaseToTest = Mono.just(disease).block();
        Mockito.when(diseaseRepository.findByOrphaCode("7")).thenReturn(diseaseToTest);
        Disease disease1 = diseaseService.getDiseaseByOrphaCode("7");
        assert(diseaseToTest.getCodes()).equals(disease1.getCodes());
        assert(diseaseToTest.getName()).equals(disease1.getName());
        assert(diseaseToTest.getSynonyms()).equals(disease1.getSynonyms());
        assert(diseaseToTest.getOrphaCode()).equals(disease1.getOrphaCode());
    }
}
