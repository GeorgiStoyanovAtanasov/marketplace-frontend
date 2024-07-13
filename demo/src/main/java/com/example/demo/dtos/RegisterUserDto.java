package com.example.demo.dtos;

import lombok.Data;

@Data
public class RegisterUserDto {

    private String email;
    private String password;
    private String confirmPassword;
    private String fullName;
}
