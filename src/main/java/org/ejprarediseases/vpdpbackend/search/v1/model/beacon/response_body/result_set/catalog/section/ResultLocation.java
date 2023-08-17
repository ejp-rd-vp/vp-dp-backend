package org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.result_set.catalog.section;

import lombok.Data;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.enums.Country;

@Data
public class ResultLocation {
    private Country country;
}
