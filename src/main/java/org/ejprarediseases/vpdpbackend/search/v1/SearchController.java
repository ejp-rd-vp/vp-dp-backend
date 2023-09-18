package org.ejprarediseases.vpdpbackend.search.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.ejprarediseases.vpdpbackend.resource.v1.model.ResourceType;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.enums.Country;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.enums.Sex;
import org.ejprarediseases.vpdpbackend.search.v1.model.SearchRequest;
import org.ejprarediseases.vpdpbackend.search.v1.model.SearchResponse;
import org.ejprarediseases.vpdpbackend.utils.UserHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@RestController
@RequestMapping("v1/search")
@RequiredArgsConstructor
@Validated
@Tag(name = "Search", description = "Endpoints for searching diseases")
public class SearchController {

    private final SearchService searchService;

    /**
     * Handles ConstraintViolationException by returning a BAD_REQUEST response with error message.
     *
     * @param e The ConstraintViolationException.
     * @return A ResponseEntity containing the error message and BAD_REQUEST status.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>
                (e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MissingServletRequestParameterException by returning a BAD_REQUEST response with error message.
     *
     * @param e The MissingServletRequestParameterException.
     * @return A ResponseEntity containing the error message and BAD_REQUEST status.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return new ResponseEntity<>
                (e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MethodArgumentTypeMismatchException by returning a BAD_REQUEST response with error message.
     *
     * @param e The MethodArgumentTypeMismatchException.
     * @return A ResponseEntity containing the error message and BAD_REQUEST status.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return new ResponseEntity<>
                (e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles the search request for diseases by OrphaCode and returns the search response.
     *
     * @param resourceTypes The list of resource types.
     * @param countries The list of countries.
     * @param sexes The list of sexes.
     * @param ageThisYear The list of age criteria for the current year.
     * @param symptomOnset The list of symptom onset ages.
     * @param ageAtDiagnoses The list of age at diagnosis criteria.
     * @param diseases The list of disease codes.
     * @param resourceId The resource ID.
     * @return A ResponseEntity containing the search response and OK status.
     */
    @Operation(
            summary = "Search Diseases",
            description = "Searches for diseases based on various criteria."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Search successful",
                    content = @Content(schema = @Schema(implementation = SearchResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request"
            ),
            @ApiResponse(
                    responseCode = "406",
                    description = "Not Acceptable"
            )
    })
    @GetMapping
    public ResponseEntity searchForDiseasesByOrphaCode(
            @Parameter(description = "List of resource types")
            @RequestParam(required = false) List<ResourceType> resourceTypes,
            @Parameter(description = "List of countries")
            @RequestParam(required = false) List<Country> countries,
            @Parameter(description = "List of sexes")
            @RequestParam(required = false) List<Sex> sexes,
            @Parameter(description = "Age criteria for the current year [min, max]")
            @RequestParam(required = false) List<Integer> ageThisYear,
            @Parameter(description = "Symptom onset ages [min, max]")
            @RequestParam(required = false) List<Integer> symptomOnset,
            @Parameter(description = "Age at diagnosis criteria [min, max]")
            @RequestParam(required = false) List<Integer> ageAtDiagnoses,
            @Parameter(description = "List of disease codes")
            @RequestParam List<String> diseases,
            @Parameter(description = "Resource ID")
            @RequestParam String resourceId
    ) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setResourceTypes(resourceTypes);
        searchRequest.setCountries(countries);
        searchRequest.setDiseases(diseases);
        searchRequest.setResourceId(resourceId);
        SearchResponse searchResponse;
        if(UserHandler.isAuthenticated() && !UserHandler.isAnonymous()) {
            searchRequest.setSexes(sexes);
            searchRequest.setAgeThisYear(ageThisYear);
            searchRequest.setSymptomOnset(symptomOnset);
            searchRequest.setAgeAtDiagnosis(ageAtDiagnoses);
        } else {
            if (Stream.of(sexes, ageThisYear, symptomOnset, ageAtDiagnoses).noneMatch(Objects::isNull)) {
                return new ResponseEntity<>(
                        "Please log in to activate these filers: sexes, ageThisYear, symptomOnset, ageAtDiagnoses",
                        HttpStatus.UNAUTHORIZED);
            }
        }
        try {
            searchResponse = searchService.searchForDiseasesByOrphaCode(searchRequest);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(searchResponse, HttpStatus.OK);
    }
}
