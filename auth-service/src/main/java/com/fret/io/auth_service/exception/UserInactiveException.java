package com.fret.io.auth_service.exception;

public class UserInactiveException extends RuntimeException {

    public UserInactiveException() {
        super("Usuário suspenso/bloqueado");
    }

    public UserInactiveException(String message) {
        super(message);
    }
}
