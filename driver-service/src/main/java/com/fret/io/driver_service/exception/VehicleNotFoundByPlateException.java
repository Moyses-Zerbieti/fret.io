package com.fret.io.driver_service.exception;

import java.util.UUID;

public class VehicleNotFoundByPlateException extends RuntimeException {

    public VehicleNotFoundByPlateException(String plate) {
        super("Veículo da placa " + plate +" não encontrado" );
    }
}
