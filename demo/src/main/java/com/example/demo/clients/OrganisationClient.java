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
    public void postOrganisation(@RequestBody OrganisationDTO organisationDTO);

    @DeleteMapping("/delete")
    void deleteOrganisation(@RequestParam("id") Integer id);

    @PutMapping("/update")
    void postUpdatedOrganisation(@RequestParam("id") Integer id, @RequestBody OrganisationDTO updatedOrganisation);

    @GetMapping("/all")
    ResponseEntity<Iterable<Organisation>> allOrganisations();
}
