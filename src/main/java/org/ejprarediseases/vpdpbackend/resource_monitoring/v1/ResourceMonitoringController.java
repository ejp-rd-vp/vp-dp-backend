package org.ejprarediseases.vpdpbackend.resource_monitoring.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model.Period;
import org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model.ResourceMonitoringSummary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.temporal.ChronoUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/monitoring")
@Tag(name = "Monitoring", description = "Endpoints for resource monitoring")
public class ResourceMonitoringController {

    private final ResourceMonitoringService monitoringService;

    /**
     * Retrieves the resource monitoring summary for a given resource ID and specified period in days.
     *
     * @param resourceId The ID of the resource for which monitoring summary is requested.
     * @param periodInDays The time period in days for which monitoring summary is requested.
     * @return A ResponseEntity containing the resource monitoring summary and an HTTP status code.
     * @see ResourceMonitoringSummary
     * @see ResponseEntity
     * @see HttpStatus
     */
    @Operation(
            summary = "Get Resource Monitoring Summary",
            description = "Retrieves the resource monitoring summary for a given resource ID and specified period."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Resource monitoring summary retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResourceMonitoringSummary.class))
            )
    })
    @GetMapping()
    public ResponseEntity getMonitoringStatus(
            @Parameter(description = "ID of the resource for monitoring")
            @RequestParam String resourceId,
            @Parameter(description = "Time period in days for monitoring")
            @RequestParam int periodInDays) {
        Period period = new Period(periodInDays, ChronoUnit.DAYS);
        ResourceMonitoringSummary summary = monitoringService.getResourceMonitoringSummary(resourceId, period);
        return new ResponseEntity<>(summary, HttpStatus.OK);
    }

}
