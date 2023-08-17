package org.ejprarediseases.vpdpbackend.mapping.v1;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GeneMapping {
    private String hgncId;
    private List<String> orphaCodes;

}
