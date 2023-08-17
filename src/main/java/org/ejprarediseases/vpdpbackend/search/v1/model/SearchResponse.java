package org.ejprarediseases.vpdpbackend.search.v1.model;

import lombok.Data;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.BeaconResponseBody;

@Data
public class SearchResponse {

    private String  resourceId;
    private String resourceName;
    private BeaconResponseBody content;
}
