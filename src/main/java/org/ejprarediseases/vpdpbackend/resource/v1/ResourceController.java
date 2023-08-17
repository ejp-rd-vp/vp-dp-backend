package org.ejprarediseases.vpdpbackend.resource.v1;


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
