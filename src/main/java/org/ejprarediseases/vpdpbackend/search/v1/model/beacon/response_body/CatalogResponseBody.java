package org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.section.catalog.BeaconResponseBodyResponseSection;


@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CatalogResponseBody extends BeaconResponseBody {
    private BeaconResponseBodyResponseSection response;

}
