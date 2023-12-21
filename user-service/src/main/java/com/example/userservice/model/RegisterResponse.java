package com.example.userservice.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private Integer userId;
    private String firstname;
    private String lastname;
    private String email;
    private String address;
    private String city;
    private Integer postcode;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;

}
