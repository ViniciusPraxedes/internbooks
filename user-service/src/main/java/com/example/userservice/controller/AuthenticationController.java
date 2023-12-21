package com.example.userservice.controller;

import com.example.userservice.model.RegisterResponse;
import com.example.userservice.service.AuthenticationService;
import com.example.userservice.model.LoginRequest;
import com.example.userservice.model.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterRequest.class))),
            @ApiResponse(responseCode = "400", description = "Bad request, one or more parameters in the request is bad formatted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Authorization required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "The code is broken", content = @Content)})
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest body){
        return new ResponseEntity<>(authenticationService.register(body), HttpStatus.OK);
    }
    @Operation(summary = "Login user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginRequest.class))),
            @ApiResponse(responseCode = "400", description = "Bad request, one or more parameters in the request is bad formatted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Authorization required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "The code is broken", content = @Content)})
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest body){
        return new ResponseEntity<>(authenticationService.login(body), HttpStatus.OK);
    }

    @Operation(summary = "Logout user(Invalidates jwt token). The jwt token is taken from swagger authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request, one or more parameters in the request is bad formatted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Authorization required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "The code is broken", content = @Content)})
    @PostMapping("/logout")
    public String logout(){
        return "Success";
    }
}
