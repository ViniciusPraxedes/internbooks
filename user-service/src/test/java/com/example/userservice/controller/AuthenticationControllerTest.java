
/*

package com.example.userservice.controller;

import com.example.userservice.logoutToken.TokenRepository;
import com.example.userservice.model.RegisterRequest;
import com.example.userservice.service.AuthenticationService;
import com.example.userservice.service.JwtService;
import com.example.userservice.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.BDDAssumptions.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.RequestEntity.post;


@WebMvcTest(controllers = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private TokenRepository tokenRepository;
    @Mock
    private UserRepository userRepository;
    @MockBean
    private AuthenticationService authenticationService;

    @Test
    public void AuthenticationController_CreateUser_ReturnResponseEntity200AndJwt() throws Exception {
        //Given
        String jwt = "JwtToken";
        RegisterRequest registerDTO = new RegisterRequest();
        registerDTO.setEmail("test@hotmail.com");
        registerDTO.setFirstname("test");
        registerDTO.setLastname("test");
        registerDTO.setPassword("test");

        given(authenticationService.register(any())).willReturn(new ResponseEntity<>(jwt,HttpStatus.OK));

        //When
        ResultActions response = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)));


        //Then
        response
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
                .andExpect(content().string(jwt));

    }
    @Test
    public void AuthenticationController_CreateUser_ReturnEmailTakenError() throws Exception {
        //Given
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail("user@hotmail.com");
        registerDTO.setFirstname("test");
        registerDTO.setLastname("test");
        registerDTO.setPassword("test");


        given(authenticationService.register(any())).willReturn(new ResponseEntity<>("Email taken", HttpStatus.BAD_REQUEST));

        //When
        ResultActions response = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)));


        //Then
        response
                .andExpect(status().isBadRequest()).andDo(MockMvcResultHandlers.print())
                .andExpect(content().string("Email taken"));
    }

    @Test
    public void AuthenticationController_LoginValidCredentials_ReturnsJwt() throws Exception {
        // Given
        String jwt = "JwtToken";
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@hotmail.com");
        loginDTO.setPassword("password");

        when(authenticationService.login(any())).thenReturn(new ResponseEntity<>(jwt, HttpStatus.OK));

        // When
        ResultActions response = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)));

        // Then
        response
                .andExpect(status().isOk()) // Expect a 200 OK response
                .andExpect(content().string(jwt)); // Expect the JWT token in the response
    }

    @Test
    public void AuthenticationController_LoginInvalidCredentials_ReturnsUnauthorized() throws Exception {
        // Given
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@hotmail.com");
        loginDTO.setPassword("invalidpassword");

        when(authenticationService.login(any())).thenReturn(new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED));

        // When
        ResultActions response = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)));

        // Then
        response
                .andExpect(status().isUnauthorized()) // Expect a 401 Unauthorized response
                .andExpect(content().string("Invalid credentials")); // Expect the error message in the response
    }







     */



