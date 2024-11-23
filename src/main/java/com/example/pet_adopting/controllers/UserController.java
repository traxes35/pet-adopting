package com.example.pet_adopting.controllers;


import com.example.pet_adopting.services.UserService;
import com.example.pet_adopting.entities.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @PostMapping
    public User createUser(@RequestBody User newUser) {
        return userService.saveOneUser(newUser);
    }
    @GetMapping("/{userId}")
    public User getOneUser(@PathVariable long userId) {
        return userService.getOneUserbyId(userId);

    }
    @PutMapping("/{userId}")
    public User updateUser(@PathVariable long userId, @RequestBody User newUser) {
        return userService.updateOneUser(userId,newUser);
    }
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId){
        userService.deleteById(userId);
    }
}