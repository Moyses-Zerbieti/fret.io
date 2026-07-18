package com.fret.io.driver_service.exception;

public class CnhAlreadyRegisteredException extends RuntimeException {

    public CnhAlreadyRegisteredException(){
        super("Número de CNH informado já cadastrado no sistema");
    }
}
