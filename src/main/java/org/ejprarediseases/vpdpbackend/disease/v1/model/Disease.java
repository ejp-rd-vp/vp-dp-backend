package org.ejprarediseases.vpdpbackend.disease.v1.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "diseases")
public class Disease {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "orphacode")
    private String orphaCode;
    @Basic
    @Column(name = "synonyms")
    private String synonyms;
    @Basic
    @Column(name = "codes")
    private String codes;

}
