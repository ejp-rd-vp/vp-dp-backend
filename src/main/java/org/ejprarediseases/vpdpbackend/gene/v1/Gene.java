package org.ejprarediseases.vpdpbackend.gene.v1;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "genes")
public class Gene {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Basic
    @Column(name = "hgnc_id")
    private String hgncId;
    @Basic
    @Column(name = "symbol")
    private String symbol;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "status")
    private String status;
    @Basic
    @Column(name = "previous_symbols")
    private String previousSymbols;
    @Basic
    @Column(name = "alias_symbols")
    private String aliasSymbols;
    @Basic
    @Column(name = "alias_names")
    private String aliasNames;
    @Basic
    @Column(name = "omim_id")
    private String omimId;
}
