package org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.section.biosample;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.result_set.biosample.BiosampleResultSet;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BeaconResponseBodyResponseSection {
    private List<BiosampleResultSet> resultSets;
}
