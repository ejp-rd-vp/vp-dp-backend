package org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.filters.ontology_filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.filters.BeaconRequestBodyFilter;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class BeaconRequestBodyOntologyFilter extends BeaconRequestBodyFilter {
    private List<String> id;
}
