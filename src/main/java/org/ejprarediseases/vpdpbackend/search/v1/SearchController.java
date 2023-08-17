package org.ejprarediseases.vpdpbackend.search.v1;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.ejprarediseases.vpdpbackend.resource.v1.model.ResourceType;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.enums.Country;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.enums.Sex;
import org.ejprarediseases.vpdpbackend.search.v1.model.SearchRequest;
import org.ejprarediseases.vpdpbackend.search.v1.model.SearchResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("v1/search")
@RequiredArgsConstructor
@Validated
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
    @GetMapping
    public ResponseEntity searchForDiseasesByOrphaCode(
            @RequestParam(required = false) List<ResourceType> resourceTypes,
            @RequestParam(required = false) List<Country> countries,
            @RequestParam(required = false) List<Sex> sexes,
            @RequestParam(required = false) List<Integer> ageThisYear,
            @RequestParam(required = false) List<Integer> symptomOnset,
            @RequestParam(required = false) List<Integer> ageAtDiagnoses,
            @RequestParam List<String> diseases,
            @RequestParam String resourceId
    ) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setResourceTypes(resourceTypes);
        searchRequest.setCountries(countries);
        searchRequest.setSexes(sexes);
        searchRequest.setAgeThisYear(ageThisYear);
        searchRequest.setSymptomOnset(symptomOnset);
        searchRequest.setAgeAtDiagnosis(ageAtDiagnoses);
        searchRequest.setDiseases(diseases);
        searchRequest.setResourceId(resourceId);
        SearchResponse searchResponse;
        try {
            searchResponse = searchService.searchForDiseasesByOrphaCode(searchRequest);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(searchResponse, HttpStatus.OK);
    }
}
