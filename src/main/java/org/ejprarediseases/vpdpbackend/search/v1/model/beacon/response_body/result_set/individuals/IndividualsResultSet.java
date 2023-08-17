package org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.result_set.individuals;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndividualsResultSet {
    private String id;
    private String type;
    private boolean exists;
    private int  resultCount;
    private IndividualsResultSetInfo info;

}
