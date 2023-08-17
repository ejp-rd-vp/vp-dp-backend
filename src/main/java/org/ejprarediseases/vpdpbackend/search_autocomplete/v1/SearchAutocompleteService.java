package org.ejprarediseases.vpdpbackend.search_autocomplete.v1;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ejprarediseases.vpdpbackend.disease.v1.model.Disease;
import org.ejprarediseases.vpdpbackend.disease.v1.DiseaseRepository;
import org.ejprarediseases.vpdpbackend.gene.v1.Gene;
import org.ejprarediseases.vpdpbackend.gene.v1.GeneRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SearchAutocompleteService {

    final GeneRepository geneRepository;
    final DiseaseRepository diseaseRepository;

    /**
     * Retrieves search results for genes and rare diseases based on the given query.
     *
     * @param query The search query.
     * @param page The page number for pagination.
     * @param size The number of results per page.
     * @return The search autocomplete results containing genes and rare diseases.
     */
    @Transactional
    public SearchAutocompleteResult getSearchResults(String query, int page, int size) {
        SearchAutocompleteResult searchResult = new SearchAutocompleteResult();
        searchResult.setGenes(getGenesSearchResults(query, page, size));
        searchResult.setRareDiseases(getDiseasesSearchResults(query, page, size));
        return searchResult;
    }

    /**
     * Retrieves genes that match the given query and pagination parameters.
     *
     * @param query The search query for genes.
     * @param page The page number for gene pagination.
     * @param size The number of gene results per page.
     * @return The set of genes matching the query and pagination.
     */
    @Transactional
    private Set<Gene> getGenesSearchResults(String query, int page, int size) {
        Set<Gene> genes = new HashSet<>();
        genes.addAll(geneRepository.findByHgncIdEqualsIgnoreCase(query, PageRequest.of(0, 1)));
        genes.addAll(geneRepository.findByOmimIdEqualsIgnoreCase(query, PageRequest.of(0, 1)));
        genes.addAll(geneRepository.findBySymbolEqualsIgnoreCase(query, PageRequest.of(0, 1)));
        if (genes.size() < 1) {
            if (query.matches("[0-9]+")) {
                genes.addAll(
                        geneRepository.findByHgncIdIgnoreCaseContaining(query, PageRequest.of(page, size)));
                genes.addAll(geneRepository.findByOmimIdIgnoreCaseContaining(query, PageRequest.of(page, size)));
            } else {
                genes.addAll(geneRepository.findByAliasNamesIgnoreCaseContaining(query, PageRequest.of(page, size)));
                genes.addAll(geneRepository.findByAliasSymbolsIgnoreCaseContaining(query, PageRequest.of(page, size)));
                genes.addAll(geneRepository.findByNameIgnoreCaseContaining(query, PageRequest.of(page, size)));
                genes.addAll(
                        geneRepository.findByPreviousSymbolsIgnoreCaseContaining(query, PageRequest.of(page, size)));
                genes.addAll(geneRepository.findBySymbolIgnoreCaseContaining(query, PageRequest.of(page, size)));
            }
        }
        return genes;
    }

    /**
     * Retrieves rare diseases that match the given query and pagination parameters.
     *
     * @param query The search query for rare diseases.
     * @param page The page number for rare disease pagination.
     * @param size The number of rare disease results per page.
     * @return The set of rare diseases matching the query and pagination.
     */
    @Transactional
    private Set<Disease> getDiseasesSearchResults(String query, int page, int size) {
        Set<Disease> rareDiseases = new HashSet<>();
        rareDiseases.addAll(diseaseRepository.findByOrphaCodeEqualsIgnoreCase(query, PageRequest.of(0, 1)));
        if (rareDiseases.size() < 1) {
            if (query.matches("[0-9]+")) {
                rareDiseases.addAll(
                        diseaseRepository.findByOrphaCodeContainingIgnoreCase(query, PageRequest.of(page, size)));
            } else {
                rareDiseases.addAll(
                        diseaseRepository.findByNameContainingIgnoreCase(query, PageRequest.of(page, size)));
                rareDiseases.addAll(
                        diseaseRepository.findBySynonymsContainingIgnoreCase(query, PageRequest.of(page, size)));
            }
            rareDiseases.addAll(diseaseRepository.findByCodesContainingIgnoreCase(query, PageRequest.of(page, size)));
        }
        return rareDiseases;
    }
}
