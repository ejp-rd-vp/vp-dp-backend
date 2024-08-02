package org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.result_set.biosample;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BiosampleResultSet {
    private String id;
    private String type;
    private boolean exists;
    private int  resultCount;
    private BiosampleResultSetInfo info;

}
