package com.example.pet_adopting.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserRequest {
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;

}
