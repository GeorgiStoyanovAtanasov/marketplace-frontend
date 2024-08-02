package com.example.demo.clients;

import com.example.demo.IndividualFeignConfig;

import com.example.demo.dtos.ManagerDTO;
import com.example.demo.dtos.OrganisationDTO;
import com.example.demo.models.Organisation;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "organisation-service", url = "${organisation-service.url}", configuration = IndividualFeignConfig.class)
public interface OrganisationClient {
    @PostMapping("/submit")
    boolean postOrganisation(@RequestBody OrganisationDTO organisationDTO,@RequestParam Integer id);

    @GetMapping("/all")
    ResponseEntity<Iterable<Organisation>> allOrganisations();
}
