package com.fret.io.driver_service.exception;

public class PlateAlreadyExistsException extends RuntimeException {

    public PlateAlreadyExistsException(){
        super("Placa já cadastrada no sistema");
    }

}
