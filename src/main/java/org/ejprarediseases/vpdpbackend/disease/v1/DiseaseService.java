package org.ejprarediseases.vpdpbackend.disease.v1;

import lombok.RequiredArgsConstructor;
import org.ejprarediseases.vpdpbackend.disease.v1.model.Disease;
import org.ejprarediseases.vpdpbackend.disease.v1.model.DiseaseDto;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DiseaseService {

    private final DiseaseRepository repository;

    /**
     * Retrieves a Disease entity from the repository based on the provided OrphaCode.
     *
     * @param orphaCode The OrphaCode used to search for a specific disease.
     * @return The Disease entity if found, or null if not found.
     */
    public Disease getDiseaseByOrphaCode(String orphaCode) {
        return repository.findByOrphaCode(orphaCode);
    }

    /**
     * Converts a Disease entity to a DiseaseDto (Data Transfer Object) for transferring disease data to clients.
     *
     * @param disease The Disease entity to be converted to a DiseaseDto.
     * @return The DiseaseDto containing essential disease information.
     */
    public DiseaseDto diseaseToDto(Disease disease) {
        DiseaseDto diseaseDto = new DiseaseDto();
        diseaseDto.setCodes(disease.getCodes());
        diseaseDto.setName(disease.getName());
        diseaseDto.setSynonyms(disease.getSynonyms());
        diseaseDto.setOrphaCode(disease.getOrphaCode());
        return diseaseDto;
    }
}
