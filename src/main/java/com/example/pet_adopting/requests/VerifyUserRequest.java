package com.example.pet_adopting.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
@Getter
@Setter
public class VerifyUserRequest {
    private String email;
    private String verificationCode;

    public VerifyUserRequest() {

    }
}
