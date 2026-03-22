package com.fret.io.auth_service;

public class DocInvalidException extends RuntimeException {
    public DocInvalidException(String message) {
        super(message);
    }
}
