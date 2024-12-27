package com.example.pet_adopting.services;

import com.example.pet_adopting.entities.User;
import com.example.pet_adopting.repos.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;

    @Before
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    public void whenGetAllUsersCalled_itShouldReturnUserList() {
        // Arrange
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        user1.setName("John Doe");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Jane Doe");

        users.add(user1);
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(users, result);
        verify(userRepository).findAll();
        System.out.println("Test Passed: Successfully fetched all users.");
    }

    @Test
    public void whenSaveOneUserCalled_itShouldSaveAndReturnUser() {
        // Arrange
        User user = new User();
        user.setName("John Doe");

        when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = userService.saveOneUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository).save(user);
        System.out.println("Test Passed: User saved successfully.");
    }

    @Test
    public void whenGetOneUserByIdCalledWithValidId_itShouldReturnUser() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("John Doe");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getOneUserbyId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository).findById(userId);
        System.out.println("Test Passed: Successfully fetched user by ID.");
    }

    @Test
    public void whenGetOneUserByIdCalledWithInvalidId_itShouldReturnNull() {
        // Arrange
        Long userId = 99L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        User result = userService.getOneUserbyId(userId);

        // Assert
        assertNull(result);
        verify(userRepository).findById(userId);
        System.out.println("Test Passed: No user found with invalid ID.");
    }

    @Test
    public void whenUpdateOneUserCalledWithValidId_itShouldReturnUpdatedUser() {
        // Arrange
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("John Doe");

        User newUser = new User();
        newUser.setName("John Updated");
        newUser.setPassword("newpassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // Act
        User result = userService.updateOneUser(userId, newUser);

        // Assert
        assertNotNull(result);
        assertEquals(newUser.getName(), result.getName());
        assertEquals(newUser.getPassword(), result.getPassword());
        verify(userRepository).findById(userId);
        verify(userRepository).save(existingUser);
        System.out.println("Test Passed: User updated successfully.");
    }

    @Test
    public void whenUpdateOneUserCalledWithInvalidId_itShouldReturnNull() {
        // Arrange
        Long userId = 99L;
        User newUser = new User();
        newUser.setName("John Updated");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        User result = userService.updateOneUser(userId, newUser);

        // Assert
        assertNull(result);
        verify(userRepository).findById(userId);
        System.out.println("Test Passed: No user found to update with invalid ID.");
    }

    @Test
    public void whenDeleteByIdCalled_itShouldDeleteUser() {
        // Arrange
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        // Act
        userService.deleteById(userId);

        // Assert
        verify(userRepository).deleteById(userId);
        System.out.println("Test Passed: User deleted successfully.");
    }
}