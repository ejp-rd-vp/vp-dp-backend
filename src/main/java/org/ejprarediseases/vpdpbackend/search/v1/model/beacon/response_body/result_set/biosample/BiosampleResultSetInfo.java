package org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.result_set.biosample;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BiosampleResultSetInfo {
    private String contactPoint;
    private String contactEmail;
    private String contactURL;
}
