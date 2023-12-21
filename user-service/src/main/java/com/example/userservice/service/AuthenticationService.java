package com.example.userservice.service;

import com.example.userservice.model.*;
import com.example.userservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private JwtService jwtService;

    public AuthenticationService(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public RegisterResponse register(RegisterRequest body){

        //Checks if user with the same email already exists in the database
        if (userRepository.findByEmail(body.getEmail()).isPresent()){

            //If user with the same email exists, return 400
            throw new IllegalStateException("Email taken");

        }else {

            //Creates user
            User user = User.builder()
                    .firstname(body.getFirstname())
                    .lastname(body.getLastname())
                    .email(body.getEmail())
                    .password(passwordEncoder.encode(body.getPassword()))
                    .address(body.getAddress())
                    .city(body.getCity())
                    .postcode(body.getPostcode())
                    .phoneNumber(body.getPhoneNumber())
                    .role(Role.USER)
                    .build();

            //Adds user to the database
            userRepository.save(user);

            RegisterResponse response = RegisterResponse.builder()
                    .userId(user.getId())
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .email(user.getEmail())
                    .address(user.getAddress())
                    .city(user.getCity())
                    .postcode(user.getPostcode())
                    .phoneNumber(user.getPhoneNumber())
                    .role(Role.USER)
                    .build();

            //Returns User and http status 200
            return response;
        }
    }

    public String login(LoginRequest body){

        //Checks if user exists in the database and if the password matches
        if (userRepository.findByEmail(body.getEmail()).isPresent() && passwordEncoder.matches(body.getPassword(), userRepository.findByEmail(body.getEmail()).get().getPassword())){

            Map<String, Object> extraClaims = new HashMap<>();
            boolean revoked = false;

            extraClaims.put("authority", userRepository.findByEmail(body.getEmail()).get().getAuthorities().toString());
            extraClaims.put("revoked", revoked);

            //Generates jwt token
            String jwt = jwtService.generateTokenWithExtraClaims(extraClaims,userRepository.findByEmail(body.getEmail()).get());

            //Return jwt token and status code 200
            return "Token is valid for 1 hour: "+jwt;

        }else {
            throw new IllegalStateException("User not found");
        }

    }


}
