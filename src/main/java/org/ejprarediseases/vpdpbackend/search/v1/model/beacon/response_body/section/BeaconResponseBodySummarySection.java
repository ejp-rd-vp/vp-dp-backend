package org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.section;

import lombok.Data;

@Data
public class BeaconResponseBodySummarySection {
    private int numTotalResults;
    private boolean exists;
}
