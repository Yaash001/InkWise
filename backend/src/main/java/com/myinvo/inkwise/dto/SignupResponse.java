package com.myinvo.inkwise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponse {

    private String userId;
    private String username;
    private String email;
    private String message;

    public SignupResponse(String userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.message = "Signup successful";
    }
}