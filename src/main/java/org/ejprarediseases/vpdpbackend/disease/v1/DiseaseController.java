package org.ejprarediseases.vpdpbackend.disease.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Disease", description = "Endpoints for retrieving disease information")
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
    @Operation(
            summary = "Get Disease by OrphaCode",
            description = "Retrieves disease information based on the provided OrphaCode."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Disease information retrieved successfully",
                    content = @Content(schema = @Schema(implementation = DiseaseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Disease not found"
            )
    })
    @GetMapping()
    public ResponseEntity getDiseaseByOrphaCode(
            @Parameter(description = "OrphaCode representing the disease to be retrieved")
            @RequestParam  String orphaCode) {
        try {
            Disease disease = service.getDiseaseByOrphaCode(orphaCode);
            DiseaseDto diseaseDto = service.diseaseToDto(disease);
            return new ResponseEntity<>(diseaseDto, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
