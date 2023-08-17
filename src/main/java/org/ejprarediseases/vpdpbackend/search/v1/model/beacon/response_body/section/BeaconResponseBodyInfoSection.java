package org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.section;

import lombok.Data;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.warning.BeaconResponseBodyWarnings;

@Data
public class BeaconResponseBodyInfoSection {
    private BeaconResponseBodyWarnings warnings;
}
