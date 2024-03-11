package org.ejprarediseases.vpdpbackend.hierarchy.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.ejprarediseases.vpdpbackend.hierarchy.v1.model.HierarchyWay;
import org.ejprarediseases.vpdpbackend.hierarchy.v1.model.OrphaCodeHierarchyDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.NoSuchElementException;


/**
 * This class represents the REST controller for handling orphanet hierarchy operations.
 * It provides endpoints to retrieve and process hierarchy related to OrphaCodes.
 */
@RestController
@RequestMapping("v1/hierarchy")
@RequiredArgsConstructor
@Validated
@Tag(name = "Hierarchy", description = "Endpoints for handling orphanet hierarchy operations")
public class HierarchyController {

    private final HierarchyService hierarchyService;

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
     * Retrieve the hierarchical representation of the OrphaCode.
     * @param ways The list of hierarchical ways (e.g. UP or DOWN) to be used.
     * @param orphaCode The OrphaCode for which the hierarchy needs to be retrieved.
     * @param numberOfLevels The number of hierarchy levels to retrieve (default: 100).
     * @return ResponseEntity containing the hierarchical representation as a list of OrphaCodeHierarchyDto objects.
     */
    @Operation(
            summary = "Get OrphaCode Hierarchy",
            description = "Retrieves the hierarchical representation of the OrphaCode."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OrphaCode hierarchy retrieved successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            array = @ArraySchema(schema =
                            @io.swagger.v3.oas.annotations.media.Schema(implementation = OrphaCodeHierarchyDto.class))
                    )
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
    @GetMapping()
    public ResponseEntity getOrphaCodeHierarchy(
            @Parameter(description = "List of hierarchical ways (e.g. UP or DOWN) to be used")
            @RequestParam @Valid List<HierarchyWay> ways,
            @Parameter(description = "OrphaCode for which the hierarchy needs to be retrieved")
            @RequestParam @Valid String orphaCode,
            @Parameter(description = "Number of hierarchy levels to retrieve (default: 100)", example = "100")
            @RequestParam(required = false, defaultValue = "100") @Valid @Min(1) int numberOfLevels
    ) {
        List<OrphaCodeHierarchyDto> response = null;
        try {
            response = hierarchyService.convertOrphaCodeHierarchyToListOfOrphaCodeDto(
                    hierarchyService.getOrphaCodeHierarchy(orphaCode, ways, numberOfLevels));
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
