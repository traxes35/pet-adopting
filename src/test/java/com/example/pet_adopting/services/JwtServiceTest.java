package com.example.pet_adopting.services;

import io.jsonwebtoken.Claims;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JwtServiceTest {

    private JwtService jwtService;

    @Before
    public void setUp() {
        jwtService = new JwtService();

        // Mocking @Value fields
        jwtService.secretKey = "mysecretkeymysecretkeymysecretkeymysecretkey"; // Replace with your test key
        jwtService.jwtExpiration = 1000 * 60 * 60; // 1 hour expiration
    }

    @Test
    public void whenGenerateTokenCalled_itShouldReturnValidToken() {
        // Arrange
        UserDetails userDetails = new User("testuser", "password", new ArrayList<>());

        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        System.out.println("Generated Token: " + token);
    }

    @Test
    public void whenExtractUsernameCalledWithValidToken_itShouldReturnUsername() {
        // Arrange
        UserDetails userDetails = new User("testuser", "password", new ArrayList<>());
        String token = jwtService.generateToken(userDetails);

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertEquals("testuser", username);
    }

    @Test
    public void whenIsTokenValidCalledWithValidToken_itShouldReturnTrue() {
        // Arrange
        UserDetails userDetails = new User("testuser", "password", new ArrayList<>());
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void whenIsTokenValidCalledWithInvalidToken_itShouldReturnFalse() {
        // Arrange
        UserDetails userDetails = new User("testuser", "password", new ArrayList<>());
        String token = jwtService.generateToken(userDetails);

        UserDetails anotherUser = new User("anotheruser", "password", new ArrayList<>());

        // Act
        boolean isValid = jwtService.isTokenValid(token, anotherUser);

        // Assert
        assertFalse(isValid);
    }


    @Test
    public void whenIsTokenExpiredCalledWithExpiredToken_itShouldReturnTrue() {
        // Arrange
        UserDetails userDetails = new User("testuser", "password", new ArrayList<>());
        jwtService.jwtExpiration = -1000; // Force token to expire immediately
        String token = jwtService.generateToken(userDetails);

        // Act & Assert
        try {
            boolean isExpired = jwtService.isTokenExpired(token);
            assertTrue(isExpired); // Süresi dolmuş token için true dönmelidir
            System.out.println("Test Passed: Token is expired as expected.");
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.out.println("Test Passed: ExpiredJwtException caught as expected.");
        }
    }

    @Test
    public void whenExtractExpirationCalled_itShouldReturnCorrectDate() {
        // Arrange
        UserDetails userDetails = new User("testuser", "password", new ArrayList<>());
        String token = jwtService.generateToken(userDetails);

        // Act
        Date expiration = jwtService.extractExpiration(token);

        // Assert
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
        System.out.println("Token Expiration: " + expiration);
    }

    @Test
    public void whenExtractClaimCalled_itShouldReturnSpecificClaim() {
        // Arrange
        UserDetails userDetails = new User("testuser", "password", new ArrayList<>());
        String token = jwtService.generateToken(userDetails);

        // Act
        Claims claims = jwtService.extractClaim(token, Function.identity());

        // Assert
        assertNotNull(claims);
        assertEquals("testuser", claims.getSubject());
    }
}
