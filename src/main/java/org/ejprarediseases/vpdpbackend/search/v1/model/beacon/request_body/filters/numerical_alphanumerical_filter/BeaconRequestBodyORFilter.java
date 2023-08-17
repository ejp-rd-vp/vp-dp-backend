package org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.filters.numerical_alphanumerical_filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.filters.BeaconRequestBodyFilter;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class BeaconRequestBodyORFilter extends BeaconRequestBodyFilter {

    private String id;
    private String operator;
    private List<String> value;
}
