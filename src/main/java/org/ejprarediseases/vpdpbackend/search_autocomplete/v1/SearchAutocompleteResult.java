package org.ejprarediseases.vpdpbackend.search_autocomplete.v1;


import lombok.Data;
import org.ejprarediseases.vpdpbackend.disease.v1.model.Disease;
import org.ejprarediseases.vpdpbackend.gene.v1.Gene;

import java.util.Set;

@Data
public class SearchAutocompleteResult {
  private Set<Gene> genes;
  private Set<Disease> rareDiseases;
}
