package org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.result_set.individuals;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndividualsResultSetInfo {
    private String contactPoint;
    private String contactEmail;
    private String contactURL;
}
