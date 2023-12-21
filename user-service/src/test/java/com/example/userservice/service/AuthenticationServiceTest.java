
/*
package com.example.userservice.service;

import com.example.userservice.model.*;
import org.junit.jupiter.api.Test;

import com.example.userservice.logoutToken.Token;
import com.example.userservice.logoutToken.TokenRepository;
import com.example.userservice.logoutToken.TokenType;
import com.example.userservice.repository.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthenticationService authService;
    @Test
    public void Register_RegisterUser_ReturnResponseEntityOKAndJWT() {
        //Given
        RegisterRequest registerRequest = new RegisterRequest();
        User testUser = new User();
        testUser.setFirstname("John");
        testUser.setLastname("Doe");
        testUser.setEmail("john@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setRole(Role.USER);

        //When
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(testUser);
        when(jwtService.generateTokenWithoutExtraClaims(any())).thenReturn("generatedJwt");
        when(tokenRepository.save(any())).thenReturn(new Token("generatedJwt", TokenType.BEARER, false, false, testUser));
        ResponseEntity<?> responseEntity = authService.register(registerRequest);

        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("generatedJwt", responseEntity.getBody());
    }
    @Test
    public void Register_RegisterUser_EmailTaken() {
        //Given
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("john@example.com");
        User existingUser = new User();
        existingUser.setFirstname("John");
        existingUser.setLastname("Doe");
        existingUser.setEmail("john@example.com");
        existingUser.setPassword("encodedPassword");
        existingUser.setRole(Role.USER);

        //When
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(existingUser));

        //Then
        assertThrows(IllegalStateException.class, () -> {
            authService.register(registerRequest);
        });
    }

    @Test
    public void Login_LoginUser_ReturnResponseEntityOkAndJWT(){
        //Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("test");
        User testUser = new User();
        testUser.setFirstname("John");
        testUser.setLastname("Doe");
        testUser.setEmail("test@example.com");
        testUser.setPassword("test");
        testUser.setRole(Role.USER);

        //When
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(testUser));

        when(passwordEncoder.matches(loginRequest.getPassword(), testUser.getPassword())).thenReturn(true);

        when(jwtService.generateTokenWithoutExtraClaims(testUser)).thenReturn("generatedJwt");

        when(tokenRepository.save(any())).thenReturn(new Token("generatedJwt", TokenType.BEARER, false, false, testUser));

        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        //Then
        ResponseEntity<String> responseEntity = (ResponseEntity<String>) authService.login(loginRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("generatedJwt", responseEntity.getBody());
    }
    @Test
    public void Login_LoginUser_UserNotFound(){
        //Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("test");

        //When
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        //Then
        assertThrows(UsernameNotFoundException.class, () -> {
            authService.login(loginRequest);
        });

    }

    @Test
    public void Login_LoginUser_InvalidPassword() {
        //Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("test");
        User testUser = new User();
        testUser.setFirstname("John");
        testUser.setLastname("Doe");
        testUser.setEmail("test@example.com");
        testUser.setPassword("test");
        testUser.setRole(Role.USER);

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(loginRequest.getPassword(), testUser.getPassword())).thenReturn(false);

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.login(loginRequest);
        });
    }
}

 */
