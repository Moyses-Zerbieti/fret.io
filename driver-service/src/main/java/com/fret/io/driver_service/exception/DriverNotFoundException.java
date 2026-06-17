package com.fret.io.driver_service.exception;

import java.util.UUID;

public class DriverNotFoundException extends RuntimeException {

    public DriverNotFoundException(UUID userId) {
        super("Motorista não encontrado para o usuario " + userId);
    }
}
