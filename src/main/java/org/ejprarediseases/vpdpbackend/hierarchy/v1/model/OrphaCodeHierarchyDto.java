package org.ejprarediseases.vpdpbackend.hierarchy.v1.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.ejprarediseases.vpdpbackend.hierarchy.v1.model.HierarchyWay;

@Data
@Valid
public class OrphaCodeHierarchyDto {
    private String label;
    @NotNull
    private String code;
    @Min(1)
    @Max(100)
    private int level;
    private HierarchyWay way;
}
