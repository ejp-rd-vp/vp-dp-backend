package org.ejprarediseases.vpdpbackend.search.v1.model;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.ejprarediseases.vpdpbackend.resource.v1.model.ResourceType;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.enums.Country;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.enums.Sex;

import java.util.List;

@Data
public class SearchRequest {

    private List<String> diseases;
    private String resourceId;
    private List<Sex> sexes;
    @Size(min = 2, max = 2)
    private List<Integer> ageThisYear;
    @Size(min = 2, max = 2)
    private List<Integer> ageAtDiagnosis;
    @Size(min = 2, max = 2)
    private List<Integer> symptomOnset;
    private List<Country> countries;
    private List<ResourceType> resourceTypes;
}
