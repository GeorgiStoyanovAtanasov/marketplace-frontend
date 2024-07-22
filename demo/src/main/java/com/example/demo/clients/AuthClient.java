package com.example.demo.clients;

import com.example.demo.responses.LoginResponse;
import com.example.demo.dtos.LoginUserDto;
import com.example.demo.dtos.RegisterUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", url = "${auth-service.url}")
public interface AuthClient {

    @PostMapping("/login")
    ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto);
    @PostMapping("/register/user")
    void register(@RequestBody RegisterUserDto registerUserDto);

}
