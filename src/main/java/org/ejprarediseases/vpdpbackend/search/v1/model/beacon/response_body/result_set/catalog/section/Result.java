package org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.result_set.catalog.section;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {
    private String id;
    private String description;
    private String homepage;
    private ResultLocation location;
    private String name;
    private String type;
}
