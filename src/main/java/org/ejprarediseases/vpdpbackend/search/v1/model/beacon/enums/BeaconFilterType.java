package org.ejprarediseases.vpdpbackend.search.v1.model.beacon.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BeaconFilterType {
    SEX("NCIT_C28421"),
    DISEASE("NCIT_C2991"),
    PHENOTYPE("SIO_010056"),
    CAUSATIVE_GENES("data_2295"),
    AGE_THIS_YEAR("NCIT_C83164"),
    SYMPTOM_ONSET("NCIT_C124353"),
    ID("id"),
    NAME("name"),
    DESCRIPTION("description"),
    ORGANISATION("organisation"),
    RESOURCE_TYPES("resourceTypes"),
    COUNTRY("country"),
    AGE_AT_DIAGNOSIS("NCIT_C156420");

    private final String value;

}
