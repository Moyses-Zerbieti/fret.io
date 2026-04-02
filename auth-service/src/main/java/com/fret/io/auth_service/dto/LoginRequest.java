package com.fret.io.auth_service.dto;

import com.fret.io.auth_service.model.User;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank (message = "Digite o email para efetuar o login")
    private String email;

    @NotBlank (message = "Digite a senha para efetuar o login")
    private String password;

    public LoginRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
