package com.example.pet_adopting.controllers;

import com.example.pet_adopting.entities.User;
import com.example.pet_adopting.requests.LoginUserRequest;
import com.example.pet_adopting.requests.RegisterUserRequest;
import com.example.pet_adopting.requests.VerifyUserRequest;
import com.example.pet_adopting.responses.LoginResponse;
import com.example.pet_adopting.services.AuthenticationService;
import com.example.pet_adopting.services.JwtService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AuthenticationControllerTest {

    private AuthenticationController authenticationController;
    private AuthenticationService authenticationService;
    private JwtService jwtService;

    @Before
    public void setUp() {
        authenticationService = Mockito.mock(AuthenticationService.class);
        jwtService = Mockito.mock(JwtService.class);
        authenticationController = new AuthenticationController(jwtService, authenticationService);
    }

    @Test
    public void whenRegisterCalledWithValidRequest_itShouldReturnRegisteredUser() {
        // Arrange
        RegisterUserRequest request = new RegisterUserRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setUsername("testuser");

        User registeredUser = new User();
        registeredUser.setId(1L);
        registeredUser.setEmail(request.getEmail());
        registeredUser.setUsername(request.getUsername());

        when(authenticationService.signup(request)).thenReturn(registeredUser);

        // Act
        ResponseEntity<User> response = authenticationController.register(request);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(registeredUser, response.getBody());
        verify(authenticationService).signup(request);
    }

    @Test
    public void whenLoginCalledWithValidRequest_itShouldReturnLoginResponse() {
        // Arrange
        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("test");
        request.setPassword("password123");

        User authenticatedUser = new User();
        authenticatedUser.setId(1L);
        authenticatedUser.setEmail(request.getUsername());

        String jwtToken = "test-jwt-token";
        long expirationTime = 3600000L; // 1 hour in milliseconds

        when(authenticationService.authenticate(request)).thenReturn(authenticatedUser);
        when(jwtService.generateToken(authenticatedUser)).thenReturn(jwtToken);
        when(jwtService.getExpirationTime()).thenReturn(expirationTime);

        // Act
        ResponseEntity<LoginResponse> response = authenticationController.authenticate(request);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(jwtToken, response.getBody().getToken());
        verify(authenticationService).authenticate(request);
        verify(jwtService).generateToken(authenticatedUser);
        verify(jwtService).getExpirationTime();
    }

    @Test
    public void whenVerifyUserCalledWithValidRequest_itShouldReturnSuccessMessage() {
        // Arrange
        VerifyUserRequest request = new VerifyUserRequest();
        request.setEmail("test@example.com");
        request.setVerificationCode("123456");

        doNothing().when(authenticationService).verifyUser(request);

        // Act
        ResponseEntity<?> response = authenticationController.verifyUser(request);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Account verified successfully", response.getBody());
        verify(authenticationService).verifyUser(request);
    }

    @Test
    public void whenVerifyUserCalledWithInvalidRequest_itShouldReturnErrorMessage() {
        // Arrange
        VerifyUserRequest request = new VerifyUserRequest();
        request.setEmail("test@example.com");
        request.setVerificationCode("wrong-code");

        doThrow(new RuntimeException("Invalid verification code")).when(authenticationService).verifyUser(request);

        // Act
        ResponseEntity<?> response = authenticationController.verifyUser(request);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid verification code", response.getBody());
        verify(authenticationService).verifyUser(request);
    }

    @Test
    public void whenResendVerificationCodeCalledWithValidEmail_itShouldReturnSuccessMessage() {
        // Arrange
        String email = "test@example.com";

        doNothing().when(authenticationService).resendVerificationCode(email);

        // Act
        ResponseEntity<?> response = authenticationController.resendVerificationCode(email);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Verification code sent", response.getBody());
        verify(authenticationService).resendVerificationCode(email);
    }

    @Test
    public void whenResendVerificationCodeCalledWithInvalidEmail_itShouldReturnErrorMessage() {
        // Arrange
        String email = "invalid@example.com";

        doThrow(new RuntimeException("Email not found")).when(authenticationService).resendVerificationCode(email);

        // Act
        ResponseEntity<?> response = authenticationController.resendVerificationCode(email);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Email not found", response.getBody());
        verify(authenticationService).resendVerificationCode(email);
    }
}
