package org.ejprarediseases.vpdpbackend.gene.v1;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface GeneRepository extends JpaRepository<Gene, Long> {
    Optional<Gene> findGeneByHgncId(String hgncId);
    List<Gene> findByHgncIdEqualsIgnoreCase(String query, Pageable pageable);
    List<Gene> findBySymbolEqualsIgnoreCase(String query, Pageable pageable);
    List<Gene> findByOmimIdEqualsIgnoreCase(String query, Pageable pageable);
    List<Gene> findByHgncIdIgnoreCaseContaining(String query, Pageable pageable);
    List<Gene> findByOmimIdIgnoreCaseContaining(String query, Pageable pageable);
    List<Gene> findBySymbolIgnoreCaseContaining(String query, Pageable pageable);
    List<Gene> findByNameIgnoreCaseContaining(String query, Pageable pageable);
    List<Gene> findByStatusIgnoreCaseContaining(String query, Pageable pageable);
    List<Gene> findByPreviousSymbolsIgnoreCaseContaining(String query, Pageable pageable);
    List<Gene> findByAliasNamesIgnoreCaseContaining(String query, Pageable pageable);
    List<Gene> findByAliasSymbolsIgnoreCaseContaining(String query, Pageable pageable);

}
