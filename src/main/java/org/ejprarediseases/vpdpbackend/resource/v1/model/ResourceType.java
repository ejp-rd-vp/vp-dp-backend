package org.ejprarediseases.vpdpbackend.resource.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ResourceType {

    @JsonProperty("patientRegistry")
    PATIENT_REGISTRY,
    @JsonProperty("bioBank")
    BIO_BANK,
    @JsonProperty("knowledgeBase")
    KNOWLEDGE_BASE,
    @JsonProperty("catalog")
    CATALOG;

}
