package com.fret.io.driver_service.exception;

import java.util.UUID;

public class VehicleNotFoundException extends RuntimeException {

    public VehicleNotFoundException(UUID vehicleId) {
        super("Veículo com id " + vehicleId +" não encontrado" );
    }
}
