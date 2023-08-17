package org.ejprarediseases.vpdpbackend.resource.v1.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;

import java.sql.Timestamp;
import java.util.List;

@Data
public class Resource {

    private String resourceName;
    private String resourceAddress;
    private String resourceHomePage;
    @Email
    @NotNull
    private String email;
    private String resourceDescription;
    private List<ResourceType> resourceType;
    private String id;
    private Timestamp created;
    private Timestamp updated;
    @URL
    private String specsURL;
    @URL
    private String logo;
    private String resourceContentType;
    private boolean queryable;
    private List<QueryType> queryType;
    private List<String> theme;


}
