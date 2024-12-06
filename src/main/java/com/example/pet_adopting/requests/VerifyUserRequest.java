package com.example.pet_adopting.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyUserRequest {
    private String email;
    private String verificationCode;
}
