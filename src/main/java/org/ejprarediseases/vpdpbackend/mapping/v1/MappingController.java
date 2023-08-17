package org.ejprarediseases.vpdpbackend.mapping.v1;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;


@RestController
@Validated
@RequestMapping("v1/mapping")
public class MappingController {

  MappingService mappingService;

  public MappingController(MappingService mappingService) {
    this.mappingService = mappingService;
  }

  /**
   * Exception handler for ConstraintViolationException.
   * @param e The ConstraintViolationException instance.
   * @return ResponseEntity containing the error message and HTTP status code.
   */
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
    return new ResponseEntity<>
            (e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  /**
   * Exception handler for MissingServletRequestParameterException.
   * @param e The MissingServletRequestParameterException instance.
   * @return ResponseEntity containing the error message and HTTP status code.
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ResponseEntity<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
    return new ResponseEntity<>
            (e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles HTTP GET request to retrieve gene mapping information for the specified HGNC ID.
   *
   * @param hgncId The HGNC (HUGO Gene Nomenclature Committee) ID of the gene to fetch mapping for.
   * @return A ResponseEntity containing the GeneMapping object as the response body with HTTP status OK (200)
   */
  @GetMapping("/gene/{hgncId}")
  public ResponseEntity getGeneMapping(@PathVariable @Valid @Size(min = 1) String hgncId) {
    return new ResponseEntity<>(mappingService.getGeneMapping(hgncId), HttpStatus.OK);
    }
}
