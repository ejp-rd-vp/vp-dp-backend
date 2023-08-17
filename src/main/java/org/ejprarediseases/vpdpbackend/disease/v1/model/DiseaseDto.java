package org.ejprarediseases.vpdpbackend.disease.v1.model;

import lombok.Data;

@Data
public class DiseaseDto {
    private String name;
    private String orphaCode;
    private String synonyms;
    private String codes;
}

