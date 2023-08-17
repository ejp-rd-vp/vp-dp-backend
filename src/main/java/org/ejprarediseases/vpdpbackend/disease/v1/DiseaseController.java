package org.ejprarediseases.vpdpbackend.disease.v1;

import lombok.RequiredArgsConstructor;
import org.ejprarediseases.vpdpbackend.disease.v1.model.Disease;
import org.ejprarediseases.vpdpbackend.disease.v1.model.DiseaseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/disease")
@RequiredArgsConstructor
@Validated
public class DiseaseController {

    private final DiseaseService service;


    /**
     * Retrieves disease information based on the provided OrphaCode.
     *
     * This endpoint allows clients to fetch disease information using the OrphaCode as a query parameter.
     * If a matching disease is found in the database, it is converted into a DiseaseDto and returned with
     * an HTTP status code of 200 (OK) in the response body. If no matching disease is found, an HTTP status
     * code of 404 (Not Found) is returned.
     *
     * @param orphaCode The OrphaCode representing the disease to be retrieved.
     * @return A ResponseEntity containing the DiseaseDto if found,
     * or an empty response with HTTP status 404 if not found.
     */
    @GetMapping()
    public ResponseEntity getDiseaseByOrphaCode(@RequestParam  String orphaCode) {
        try {
            Disease disease = service.getDiseaseByOrphaCode(orphaCode);
            DiseaseDto diseaseDto = service.diseaseToDto(disease);
            return new ResponseEntity<>(diseaseDto, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
