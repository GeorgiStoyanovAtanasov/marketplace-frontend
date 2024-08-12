package com.example.demo.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterUserDto {
    @NotEmpty(message = "email can not be empty")
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String confirmPassword;
    @NotEmpty
    private String fullName;
}
