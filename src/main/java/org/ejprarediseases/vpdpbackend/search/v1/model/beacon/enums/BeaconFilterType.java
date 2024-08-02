package org.ejprarediseases.vpdpbackend.search.v1.model.beacon.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BeaconFilterType {
    SEX("obo:NCIT_C28421"),
    DISEASE("obo:NCIT_C2991"),
    PHENOTYPE("sio:SIO_010056"),
    CAUSATIVE_GENES("edam:data_2295"),
    AGE_THIS_YEAR("obo:NCIT_C83164"),
    SYMPTOM_ONSET("obo:NCIT_C124353"),
    ID("id"),
    NAME("name"),
    DESCRIPTION("description"),
    ORGANISATION("organisation"),
    RESOURCE_TYPES("resourceTypes"),
    COUNTRY("country"),
    AGE_AT_DIAGNOSIS("obo:NCIT_C156420"),
    BIOSPECIMEN_TYPE("obo:NCIT_C70713");

    private final String value;

}
