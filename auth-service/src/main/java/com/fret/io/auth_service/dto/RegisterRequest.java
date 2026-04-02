package com.fret.io.auth_service.dto;

import com.fret.io.auth_service.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {

    @NotBlank (message = "É necessário número do documento para se registrar")
    private String document;

    @NotBlank(message = "É necessário email para se registrar")
    @Email
    private String email;

    @NotBlank (message = "É necessário criar uma senha para se registrar")
    private String password;

    public User toModel(){
        return new User(document, email);
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
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
