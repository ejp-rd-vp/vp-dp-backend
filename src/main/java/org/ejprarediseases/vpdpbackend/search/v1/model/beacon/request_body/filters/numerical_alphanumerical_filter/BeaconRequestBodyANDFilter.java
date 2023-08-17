package org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.filters.numerical_alphanumerical_filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.filters.BeaconRequestBodyFilter;

@Data
@EqualsAndHashCode(callSuper=false)
public class BeaconRequestBodyANDFilter extends BeaconRequestBodyFilter {
    private String id;
    private String operator;
    private String value;
}
