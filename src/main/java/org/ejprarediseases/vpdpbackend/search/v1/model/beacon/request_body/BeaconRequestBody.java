package org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body;

import lombok.Data;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.sections.BeaconRequestBodyMetaSection;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.sections.BeaconRequestBodyQuerySection;

@Data
public class BeaconRequestBody {

    private BeaconRequestBodyMetaSection meta;
    private BeaconRequestBodyQuerySection query;

}
