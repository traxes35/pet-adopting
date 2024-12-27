package com.example.pet_adopting.config;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SecurityConfigurationTest {

    private SecurityConfiguration securityConfiguration;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private AuthenticationProvider authenticationProvider;

    @Before
    public void setUp() {
        jwtAuthenticationFilter = Mockito.mock(JwtAuthenticationFilter.class);
        authenticationProvider = Mockito.mock(AuthenticationProvider.class);
        securityConfiguration = new SecurityConfiguration(jwtAuthenticationFilter, authenticationProvider);
    }

    @Test
    public void whenSecurityFilterChainCalled_itShouldReturnNonNullChain() throws Exception {
        // Arrange
        HttpSecurity httpSecurity = mock(HttpSecurity.class);
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.authenticationProvider(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), eq(UsernamePasswordAuthenticationFilter.class))).thenReturn(httpSecurity);

        // Act
        SecurityFilterChain securityFilterChain = securityConfiguration.securityFilterChain(httpSecurity);

        // Assert
        assertNotNull(securityFilterChain);
        System.out.println("Test Passed: SecurityFilterChain is not null.");
    }

    @Test
    public void whenCorsConfigurationSourceCalled_itShouldReturnValidCorsConfiguration() {
        // Act
        CorsConfigurationSource corsConfigurationSource = securityConfiguration.corsConfigurationSource();
        CorsConfiguration corsConfiguration = corsConfigurationSource.getCorsConfiguration(null);

        // Assert
        assertNotNull(corsConfiguration);
        assertTrue(corsConfiguration.getAllowedOriginPatterns().contains("*"));
        assertTrue(corsConfiguration.getAllowedMethods().contains("GET"));
        assertTrue(corsConfiguration.getAllowedMethods().contains("POST"));
        assertTrue(corsConfiguration.getAllowedHeaders().contains("Authorization"));
        System.out.println("Test Passed: CorsConfigurationSource is valid.");
    }
}
