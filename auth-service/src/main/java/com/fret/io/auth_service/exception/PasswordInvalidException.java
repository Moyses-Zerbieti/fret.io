package com.fret.io.auth_service.exception;

import java.util.List;

public class PasswordInvalidException extends RuntimeException{
    private final List<String> errors;

    public PasswordInvalidException(List<String> errors) {
        super("Senha inválida");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
