package com.example.pet_adopting.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
@Getter
@Setter
public class RegisterUserRequest {
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;

    public RegisterUserRequest() {

    }
}
