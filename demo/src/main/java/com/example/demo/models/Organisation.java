package com.example.demo.models;

import com.example.demo.enums.OrganisationPermission;
import lombok.Data;

@Data
public class Organisation {
    private Integer id;

    private String name;

    private OrganisationPermission organisationPermission;
}

