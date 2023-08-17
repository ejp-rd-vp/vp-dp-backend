package org.ejprarediseases.vpdpbackend.hierarchy.v1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class Hierarchy {
    private String apiVersion;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<HierarchyLevel> parents;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<HierarchyLevel> childs;

    public final List<HierarchyLevel> getParents(int level) {
        return parents.stream().filter(parent -> parent.getLevel() <= level).toList();
    }

    public final List<HierarchyLevel> getChilds(int level) {
        return childs.stream().filter(child -> child.getLevel() <= level).toList();
    }

}
