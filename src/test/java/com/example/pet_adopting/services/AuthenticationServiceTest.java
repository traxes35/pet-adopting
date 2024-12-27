package com.example.pet_adopting.services;

import com.example.pet_adopting.entities.User;
import com.example.pet_adopting.repos.UserRepository;
import com.example.pet_adopting.requests.LoginUserRequest;
import com.example.pet_adopting.requests.RegisterUserRequest;
import com.example.pet_adopting.requests.VerifyUserRequest;
import jakarta.mail.MessagingException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    private UserRepository userRepository;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private EmailService emailService;

    @Before
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = Mockito.mock(UserService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationManager = Mockito.mock(AuthenticationManager.class);
        emailService = Mockito.mock(EmailService.class);

        authenticationService = new AuthenticationService(
                userRepository,
                userService,
                authenticationManager,
                passwordEncoder,
                emailService
        );
    }

    @Test
    public void whenSignupCalledWithValidInput_itShouldReturnUser() {
        // Arrange
        RegisterUserRequest request = new RegisterUserRequest("John", "Doe", "johndoe", "john@example.com", "password123");
        User user = new User(request.getName(), request.getSurname(), request.getUsername(), request.getEmail(), "encodedPassword");
        user.setVerificationCode("123456");

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userService.saveOneUser(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L); // ID atanıyor
            return savedUser;
        });

        // Act
        User result = authenticationService.signup(request);

        // Assert
        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getEmail(), result.getEmail());
        verify(userService).saveOneUser(any(User.class));
        verify(userService).assignRoleToUser(1L, "ROLE_USER");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void whenAuthenticateCalledWithInvalidUsername_itShouldThrowException() {
        // Arrange
        LoginUserRequest request = new LoginUserRequest("invalidUser", "password123");
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());

        // Act
        authenticationService.authenticate(request);

        // Assert
        // Exception is expected
    }

    @Test(expected = RuntimeException.class)
    public void whenAuthenticateCalledForInactiveUser_itShouldThrowException() {
        // Arrange
        LoginUserRequest request = new LoginUserRequest("johndoe", "password123");
        User inactiveUser = new User();
        inactiveUser.setUsername(request.getUsername());
        inactiveUser.setActive(false);

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(inactiveUser));

        // Act
        authenticationService.authenticate(request);

        // Assert
        // Exception is expected
    }

    @Test
    public void whenAuthenticateCalledWithValidInput_itShouldAuthenticateSuccessfully() {
        // Arrange
        LoginUserRequest request = new LoginUserRequest("johndoe", "password123");
        User user = new User();
        user.setUsername(request.getUsername());
        user.setActive(true);

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenAnswer(invocation -> null); // Void metodun işlevselliğini mock'luyoruz

        // Act
        User result = authenticationService.authenticate(request);

        // Assert
        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        System.out.println("Test Passed: User authenticated successfully.");
    }

    @Test
    public void whenVerifyUserCalledWithValidCode_itShouldActivateUser() {
        // Arrange
        VerifyUserRequest request = new VerifyUserRequest("john@example.com", "123456");
        User user = new User();
        user.setEmail(request.getEmail());
        user.setVerificationCode(request.getVerificationCode());
        user.setVerificationExpiryDate(LocalDateTime.now().plusMinutes(10));

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        // Act
        authenticationService.verifyUser(request);

        // Assert
        assertTrue(user.isActive());
        assertNull(user.getVerificationCode());
        assertNull(user.getVerificationExpiryDate());
        verify(userRepository).save(user);
    }

    @Test(expected = RuntimeException.class)
    public void whenVerifyUserCalledWithExpiredCode_itShouldThrowException() {
        // Arrange
        VerifyUserRequest request = new VerifyUserRequest("john@example.com", "123456");
        User user = new User();
        user.setEmail(request.getEmail());
        user.setVerificationCode(request.getVerificationCode());
        user.setVerificationExpiryDate(LocalDateTime.now().minusMinutes(1));

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        // Act
        authenticationService.verifyUser(request);

        // Assert
        // Exception is expected
    }
}
