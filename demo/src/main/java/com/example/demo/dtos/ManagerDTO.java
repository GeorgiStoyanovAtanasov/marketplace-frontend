package com.example.demo.dtos;

import lombok.Data;

@Data
public class ManagerDTO {
    private Integer id;
    private OrganisationDTO organisation;
    private UserDTO user;
}
