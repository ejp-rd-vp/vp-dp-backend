package org.ejprarediseases.vpdpbackend.resource.v1;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.ejprarediseases.vpdpbackend.resource.v1.model.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/resources")
@Tag(name = "Resources", description = "Endpoints for retrieving resources")
public class ResourceController {

    private final ResourceService resourceService;

    /**
     * Endpoint to retrieve all resources.
     *
     * @return ResponseEntity containing a list of Resource objects in the response body
     *         with HTTP status 200 (OK) if successful.
     *         If there's an error reading the resources file, returns a ResponseEntity
     *         with the error message in the response body and HTTP status 406 (NOT_ACCEPTABLE).
     */
    @Operation(
            summary = "Get All Resources",
            description = "Retrieves a list of all resources."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Resources retrieved successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            array = @ArraySchema(schema =
                            @io.swagger.v3.oas.annotations.media.Schema(implementation = Resource.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "406",
                    description = "Not Acceptable"
            )
    })
    @GetMapping()
    public ResponseEntity getResources() {
        List<Resource> resources;
        try {
            resources = resourceService.getAllResources();
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }


}
