package com.fret.io.auth_service.dto;

import jakarta.validation.constraints.Email;

public class PasswordResetRequest {

    @Email
    private String email;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
