package com.example.demo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", url = "http://localhost:8080/auth")
public interface AuthClient {

    @PostMapping("/login")
    ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto);
}
