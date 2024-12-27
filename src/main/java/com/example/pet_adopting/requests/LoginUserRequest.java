package com.example.pet_adopting.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
@Getter
@Setter
public class LoginUserRequest {
    private String username;
    private String password;

    public LoginUserRequest() {

    }
}
