package org.ejprarediseases.vpdpbackend.gene.v1;

import org.ejprarediseases.vpdpbackend.db.DatabaseSetupExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@Tag("UnitTest")
@ExtendWith(DatabaseSetupExtension.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Gene Repository Unit Tests")
public class GeneRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    GeneRepository repository;


    @Test
    public void shouldFindGeneByHgncId() {
        Gene gene = new Gene();
        gene.setHgncId("123");
        gene.setName("TeStNAme");
        entityManager.persist(gene);
        Optional<Gene> optionalGene = repository.findGeneByHgncId("123");
        assertThat(optionalGene).isPresent();
        assertThat(optionalGene.get()).isEqualTo(gene);
    }

    @Test
    public void shouldFindGeneByHgncIdIgnoreCase() {
        Gene gene = new Gene();
        gene.setHgncId("AaBb");
        gene.setName("TeStNAme");
        entityManager.persist(gene);
        List<Gene> genes = repository.findByHgncIdEqualsIgnoreCase("aAbB", PageRequest.of(0, 1));
        assertThat(genes).hasSize(1).contains(gene);
    }

    @Test
    public void shouldFindGeneBySymbolIgnoreCase() {
        Gene gene = new Gene();
        gene.setHgncId("AaBb");
        gene.setName("TeStNAme");
        gene.setSymbol("AaBb");
        entityManager.persist(gene);
        List<Gene> genes = repository.findBySymbolEqualsIgnoreCase("aAbB", PageRequest.of(0, 1));
        assertThat(genes).hasSize(1).contains(gene);
    }

    @Test
    public void shouldFindGeneByOmimIdIgnoreCase() {
        Gene gene = new Gene();
        gene.setHgncId("AaBb");
        gene.setName("TeStNAme");
        gene.setOmimId("AaBb");
        entityManager.persist(gene);
        List<Gene> genes = repository.findByOmimIdEqualsIgnoreCase("aAbB", PageRequest.of(0, 1));
        assertThat(genes).hasSize(1).contains(gene);
    }

    @Test
    public void shouldFindGeneByHgncIdContainingIgnoreCase() {
        Gene gene = new Gene();
        gene.setHgncId("AaBb");
        gene.setName("TeStNAme");
        gene.setOmimId("AaBb");
        entityManager.persist(gene);
        List<Gene> genes = repository.findByHgncIdIgnoreCaseContaining("aAb", PageRequest.of(0, 1));
        assertThat(genes).hasSize(1).contains(gene);
    }

    @Test
    public void shouldFindGeneByOmimIdContainingIgnoreCase() {
        Gene gene = new Gene();
        gene.setHgncId("AaBb");
        gene.setName("TeStNAme");
        gene.setOmimId("AaBb");
        entityManager.persist(gene);
        List<Gene> genes = repository.findByOmimIdIgnoreCaseContaining("aAb", PageRequest.of(0, 1));
        assertThat(genes).hasSize(1).contains(gene);
    }

    @Test
    public void shouldFindGeneBySymbolContainingIgnoreCase() {
        Gene gene = new Gene();
        gene.setHgncId("AaBb");
        gene.setName("TeStNAme");
        gene.setSymbol("AaBb");
        entityManager.persist(gene);
        List<Gene> genes = repository.findBySymbolIgnoreCaseContaining("aAb", PageRequest.of(0, 1));
        assertThat(genes).hasSize(1).contains(gene);
    }

    @Test
    public void shouldFindGeneByNameContainingIgnoreCase() {
        Gene gene = new Gene();
        gene.setHgncId("AaBb");
        gene.setName("AaBb");
        gene.setSymbol("AaBb");
        entityManager.persist(gene);
        List<Gene> genes = repository.findByNameIgnoreCaseContaining("aAb", PageRequest.of(0, 1));
        assertThat(genes).hasSize(1).contains(gene);
    }

    @Test
    public void shouldFindGeneByStatusContainingIgnoreCase() {
        Gene gene = new Gene();
        gene.setHgncId("AaBb");
        gene.setName("TeStNAme");
        gene.setStatus("Approved");
        entityManager.persist(gene);
        List<Gene> genes = repository.findByStatusIgnoreCaseContaining("app", PageRequest.of(0, 1));
        assertThat(genes).hasSize(1).contains(gene);
    }

    @Test
    public void shouldFindGeneByPreviousSymbolsContainingIgnoreCase() {
        Gene gene = new Gene();
        gene.setHgncId("AaBb");
        gene.setName("TeStNAme");
        gene.setPreviousSymbols("AaBb, AA");
        entityManager.persist(gene);
        List<Gene> genes =
                repository.findByPreviousSymbolsIgnoreCaseContaining("aAb", PageRequest.of(0, 1));
        assertThat(genes).hasSize(1).contains(gene);
    }

    @Test
    public void shouldFindGeneByAliasNamesContainingIgnoreCase() {
        Gene gene = new Gene();
        gene.setHgncId("AaBb");
        gene.setName("TeStNAme");
        gene.setAliasNames("AaBb, AA");
        entityManager.persist(gene);
        List<Gene> genes =
                repository.findByAliasNamesIgnoreCaseContaining("aAb", PageRequest.of(0, 1));
        assertThat(genes).hasSize(1).contains(gene);
    }

    @Test
    public void shouldFindGeneByAliasSymbolsContainingIgnoreCase() {
        Gene gene = new Gene();
        gene.setHgncId("AaBb");
        gene.setName("TeStNAme");
        gene.setAliasSymbols("AaBb, AA");
        entityManager.persist(gene);
        List<Gene> genes =
                repository.findByAliasSymbolsIgnoreCaseContaining("aAb", PageRequest.of(0, 1));
        assertThat(genes).hasSize(1).contains(gene);
    }

}
