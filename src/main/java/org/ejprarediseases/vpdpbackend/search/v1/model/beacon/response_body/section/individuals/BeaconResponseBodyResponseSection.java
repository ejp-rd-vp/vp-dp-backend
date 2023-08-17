package org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.section.individuals;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.result_set.individuals.IndividualsResultSet;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BeaconResponseBodyResponseSection {
    private List<IndividualsResultSet> resultSets;

}
