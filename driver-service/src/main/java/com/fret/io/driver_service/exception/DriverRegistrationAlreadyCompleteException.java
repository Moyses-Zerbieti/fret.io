package com.fret.io.driver_service.exception;

public class DriverRegistrationAlreadyCompleteException extends RuntimeException {

    public DriverRegistrationAlreadyCompleteException() {
        super("Cadastro do motorista ja foi concluído.");
    }
}
