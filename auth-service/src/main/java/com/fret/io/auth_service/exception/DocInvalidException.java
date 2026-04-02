package com.fret.io.auth_service.exception;

import java.util.Collections;
import java.util.List;

public class DocInvalidException extends RuntimeException {

    private final List<String> errors;

    public DocInvalidException(String errors) {
        this.errors = Collections.singletonList(errors);
    }

    public List<String> getErrors() {
        return errors;
    }
}
