package org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.sections;

import lombok.Data;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.filters.BeaconRequestBodyFilter;

import java.util.List;

@Data
public class BeaconRequestBodyQuerySection {
    private List<BeaconRequestBodyFilter> filters;
}
