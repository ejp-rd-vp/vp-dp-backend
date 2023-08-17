package org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceMonitoringSummary {
    private String resourceId;
    private Period period;
    private int numberOfTests;
    private int averageResponseTimeInMilliSeconds;
    private int numberOfInformationalResponses;
    private int numberOfSuccessfulResponses;
    private int numberOfRedirectionResponses;
    private int numberOfClientErrorResponses;
    private int numberOfServerErrorResponses;

}
