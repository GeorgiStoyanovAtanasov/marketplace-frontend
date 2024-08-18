package com.example.demo.clients;

import com.example.demo.IndividualFeignConfig;
import com.example.demo.dtos.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-service", url = "${user-service.url}", configuration = IndividualFeignConfig.class)
public interface UserClient {
    @GetMapping("/me")
    UserDTO authenticatedUser();
    @GetMapping("/roles")
    ResponseEntity<List<String>> getRoles(@RequestParam("token") String token);
    @GetMapping("/email")
    ResponseEntity<String> getEmail();
}

