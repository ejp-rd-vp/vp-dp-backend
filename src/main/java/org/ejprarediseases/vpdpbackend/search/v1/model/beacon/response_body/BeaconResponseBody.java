package org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body;

import lombok.Data;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.section.BeaconResponseBodyInfoSection;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.section.BeaconResponseBodyMetaSection;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.section.BeaconResponseBodySummarySection;

@Data
public abstract class BeaconResponseBody {
    private BeaconResponseBodyMetaSection meta;
    private BeaconResponseBodyInfoSection info;
    private BeaconResponseBodySummarySection responseSummary;
}
