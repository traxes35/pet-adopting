package com.example.pet_adopting.controllers;

import com.example.pet_adopting.entities.User;
import com.example.pet_adopting.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserController userController;
    private UserService userService;

    @Before
    public void setUp() {
        userService = Mockito.mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenGetAllUsersCalled_andUsersExist_itShouldReturnUserList() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setName("John Doe");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Jane Doe");

        List<User> users = List.of(user1, user2);
        when(userService.getAllUsers()).thenReturn(users);

        // Act
        ResponseEntity<List<User>> response = userController.getAllUsers();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(users, response.getBody());
        verify(userService).getAllUsers();
        System.out.println("Test Passed: Successfully fetched all users.");
    }

    @Test
    public void whenGetAllUsersCalled_andNoUsersExist_itShouldReturnNotFound() {
        // Arrange
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<User>> response = userController.getAllUsers();

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(userService).getAllUsers();
        System.out.println("Test Passed: No users found as expected.");
    }

    @Test
    public void whenGetOneUserCalledWithValidId_itShouldReturnUser() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("John Doe");

        when(userService.getOneUserbyId(userId)).thenReturn(user);

        // Act
        ResponseEntity<User> response = userController.getOneUser(userId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
        verify(userService).getOneUserbyId(userId);
        System.out.println("Test Passed: Successfully fetched user with ID " + userId + ".");
    }

    @Test
    public void whenGetOneUserCalledWithInvalidId_itShouldReturnNotFound() {
        // Arrange
        Long userId = 99L;
        when(userService.getOneUserbyId(userId)).thenReturn(null);

        // Act
        ResponseEntity<User> response = userController.getOneUser(userId);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(userService).getOneUserbyId(userId);
        System.out.println("Test Passed: No user found with ID " + userId + " as expected.");
    }

    @Test
    public void whenUpdateUserCalledWithValidId_itShouldReturnUpdatedUser() {
        // Arrange
        Long userId = 1L;
        User newUser = new User();
        newUser.setName("John Updated");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setName(newUser.getName());

        when(userService.updateOneUser(userId, newUser)).thenReturn(updatedUser);

        // Act
        ResponseEntity<User> response = userController.updateUser(userId, newUser);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedUser, response.getBody());
        verify(userService).updateOneUser(userId, newUser);
        System.out.println("Test Passed: Successfully updated user with ID " + userId + ".");
    }

    @Test
    public void whenUpdateUserCalledWithInvalidId_itShouldReturnNotFound() {
        // Arrange
        Long userId = 99L;
        User newUser = new User();
        newUser.setName("Non-existent User");

        when(userService.updateOneUser(userId, newUser)).thenReturn(null);

        // Act
        ResponseEntity<User> response = userController.updateUser(userId, newUser);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(userService).updateOneUser(userId, newUser);
        System.out.println("Test Passed: No user found to update with ID " + userId + " as expected.");
    }

    @Test
    public void whenDeleteUserCalledWithValidId_itShouldReturnSuccessMessage() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userService.getOneUserbyId(userId)).thenReturn(user);

        // Act
        ResponseEntity<?> response = userController.deleteUser(userId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User with ID " + userId + " has been deleted successfully.", response.getBody());
        verify(userService).getOneUserbyId(userId);
        verify(userService).deleteById(userId);
        System.out.println("Test Passed: Successfully deleted user with ID " + userId + ".");
    }

    @Test
    public void whenDeleteUserCalledWithInvalidId_itShouldReturnNotFound() {
        // Arrange
        Long userId = 99L;
        when(userService.getOneUserbyId(userId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = userController.deleteUser(userId);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User with ID " + userId + " not found.", response.getBody());
        verify(userService).getOneUserbyId(userId);
        verify(userService, never()).deleteById(userId);
        System.out.println("Test Passed: No user found to delete with ID " + userId + " as expected.");
    }
}
