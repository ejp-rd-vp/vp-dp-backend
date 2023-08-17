package org.ejprarediseases.vpdpbackend.disease.v1;

import org.ejprarediseases.vpdpbackend.disease.v1.model.Disease;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Long> {

    Disease findByOrphaCode(String orphaCode);
    List<Disease> findByOrphaCodeEqualsIgnoreCase(String query, Pageable pageable);
    List<Disease> findByNameContainingIgnoreCase(String query, Pageable pageable);
    List<Disease> findByOrphaCodeContainingIgnoreCase(String query, Pageable pageable);
    List<Disease> findBySynonymsContainingIgnoreCase(String query, Pageable pageable);
    List<Disease> findByCodesContainingIgnoreCase(String query, Pageable pageable);
}
