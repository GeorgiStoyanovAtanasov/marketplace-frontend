package com.example.demo.clients;

import com.example.demo.IndividualFeignConfig;
import com.example.demo.dtos.ManagerDTO;
import com.example.demo.dtos.RegisterUserDto;
import com.example.demo.models.Organisation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "manger-service", url = "${manager-service.url}", configuration = IndividualFeignConfig.class)
public interface ManagerClient {
    @PostMapping("/register")
    ResponseEntity<ManagerDTO> registerManager(@RequestBody RegisterUserDto registerUserDto);
    @GetMapping("/organisation")
    ResponseEntity<Organisation> findOrganisationByManager();

}
