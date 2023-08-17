package org.ejprarediseases.vpdpbackend.resource_monitoring.v1;

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
    @GetMapping()
    public ResponseEntity getMonitoringStatus(@RequestParam String resourceId, @RequestParam int periodInDays) {
        Period period = new Period(periodInDays, ChronoUnit.DAYS);
        ResourceMonitoringSummary summary = monitoringService.getResourceMonitoringSummary(resourceId, period);
        return new ResponseEntity<>(summary, HttpStatus.OK);
    }

}
