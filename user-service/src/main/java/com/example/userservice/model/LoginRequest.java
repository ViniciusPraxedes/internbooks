package com.example.userservice.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email address")
    private String email;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, max = 15, message = "Password must be between 6 and 15 characters")
    private String password;
}
