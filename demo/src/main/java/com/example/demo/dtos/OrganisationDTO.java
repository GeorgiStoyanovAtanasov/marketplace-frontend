package com.example.demo.dtos;

import com.example.demo.enums.OrganisationPermission;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class OrganisationDTO {
    private Integer id;

    private String name;
    private OrganisationPermission organisationPermission;
}

