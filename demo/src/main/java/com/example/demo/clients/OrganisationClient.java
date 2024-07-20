package com.example.demo.clients;

import com.example.demo.IndividualFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@FeignClient(name = "organisation-service", url = "http://localhost:8080/organisation", configuration = IndividualFeignConfig.class)
public interface OrganisationClient  {
    @GetMapping("/all")
    ResponseEntity<Iterable<>> allOrganisations();
}
