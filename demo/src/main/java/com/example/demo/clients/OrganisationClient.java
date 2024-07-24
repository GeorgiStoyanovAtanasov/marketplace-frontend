package com.example.demo.clients;

import com.example.demo.IndividualFeignConfig;
import com.example.demo.dtos.EventDTO;
import com.example.demo.dtos.OrganisationDTO;
import com.example.demo.models.Organisation;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "organisation-service", url = "${organisation-service.url}", configuration = IndividualFeignConfig.class)
public interface OrganisationClient {
    @PostMapping("/submit")
    public void postOrganisation(@RequestBody OrganisationDTO organisationDTO);

    @GetMapping("/all")
    ResponseEntity<Iterable<Organisation>> allOrganisations();
}
