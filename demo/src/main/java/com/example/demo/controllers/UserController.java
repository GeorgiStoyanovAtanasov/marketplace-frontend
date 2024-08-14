package com.example.demo.controllers;

import com.example.demo.Services.AuthService;
import com.example.demo.clients.UserClient;
import com.example.demo.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {
    UserClient userClient;
    AuthService authService;

    @Autowired
    public UserController(UserClient userClient, AuthService authService) {
        this.userClient = userClient;
        this.authService = authService;
    }

    @GetMapping("/me")
    public String authenticatedUser(Model model) {
        authService.getRoles(model);
        UserDTO user = userClient.authenticatedUser();
        model.addAttribute("user", user);
        return "user/profile";
    }
}
