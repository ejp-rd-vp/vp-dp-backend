package org.ejprarediseases.vpdpbackend.hierarchy.v1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
@Valid
public class HierarchyLevel {
    private int level;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<OrphaCode> parents;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<OrphaCode> childs;
}
