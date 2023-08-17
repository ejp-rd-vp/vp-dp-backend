package org.ejprarediseases.vpdpbackend.search.v1.model.beacon.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BeaconFilterOperator {
    EQUAL("="),
    LESS_OR_EQUAL("<="),
    GREATER_OR_EQUAL(">=");
    private final String value;
}
