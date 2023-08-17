package org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.section;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BeaconResponseBodyMetaSection {
    private String beaconId;
    private String apiVersion;
    private String returnedGranularity;
}
