package org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.warning;

import lombok.Data;

import java.util.List;

@Data
public class BeaconResponseBodyWarnings {
    private List<String> unsupportedFilters;
}
