package org.ejprarediseases.vpdpbackend.disease.v1;

import org.ejprarediseases.vpdpbackend.disease.v1.model.Disease;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Tag("UnitTest")
@DisplayName("Disease Repository Unit Tests")
public class DiseaseRepositoryTest {


    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    DiseaseRepository repository;

    @Test
    public void shouldFindNoDiseasesIfRepositoryIsEmpty() {
        Iterable<Disease> diseases = repository.findAll();
        assertThat(diseases).isEmpty();
    }

    @Test
    public void shouldFindDiseaseByOrphaCode() {
        Disease disease1 = new Disease();
        disease1.setName("disease1");
        disease1.setSynonyms("disease_1");
        disease1.setCodes("Q7");
        disease1.setOrphaCode("AA11");
        entityManager.persist(disease1);
        List<Disease> diseases =
                repository.findByOrphaCodeEqualsIgnoreCase("Aa11", PageRequest.of(0, 1));
        assertThat(diseases).hasSize(1).contains(disease1);
    }

    @Test
    public void shouldFindDiseaseNameContaining() {
        Disease disease = new Disease();
        disease.setName("Disease1");
        disease.setSynonyms("disease_1");
        disease.setCodes("Q7");
        disease.setOrphaCode("AA11");
        entityManager.persist(disease);
        List<Disease> diseases =
                repository.findByNameContainingIgnoreCase("dis", PageRequest.of(0, 1));
        assertThat(diseases).hasSize(1).contains(disease);
    }

    @Test
    public void shouldFindOrphaCodeContaining() {
        Disease disease = new Disease();
        disease.setName("Disease");
        disease.setSynonyms("disease");
        disease.setCodes("Q7");
        disease.setOrphaCode("AA11");
        entityManager.persist(disease);
        List<Disease> diseases =
                repository.findByOrphaCodeContainingIgnoreCase("a1", PageRequest.of(0, 1));
        assertThat(diseases).hasSize(1).contains(disease);
    }

    @Test
    public void shouldFindSynonymsContaining() {
        Disease disease = new Disease();
        disease.setName("Disease");
        disease.setSynonyms("diseaseO");
        disease.setCodes("Q7a");
        disease.setOrphaCode("AA11");
        entityManager.persist(disease);
        List<Disease> diseases =
                repository.findBySynonymsContainingIgnoreCase("seo", PageRequest.of(0, 1));
        assertThat(diseases).hasSize(1).contains(disease);
    }

    @Test
    public void shouldFindCodesContaining() {
        Disease disease = new Disease();
        disease.setName("Disease");
        disease.setSynonyms("diseaseO");
        disease.setCodes("Q7a");
        disease.setOrphaCode("AA11");
        entityManager.persist(disease);
        List<Disease> diseases =
                repository.findByCodesContainingIgnoreCase("q7A", PageRequest.of(0, 1));
        assertThat(diseases).hasSize(1).contains(disease);
    }
}
