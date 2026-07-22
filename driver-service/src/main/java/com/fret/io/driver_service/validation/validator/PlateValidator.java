package com.fret.io.driver_service.validation.validator;

import jakarta.validation.ValidationException;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public final class PlateValidator {

    private static final Pattern PLATE_PATTERN = Pattern.compile("^[A-Z]{3}-?(\\d{4}|\\d[A-Z]\\d{2})$");

    private PlateValidator(){}

    public static String validateAndNormalize(String plate){
        if (!StringUtils.hasText(plate)){
            throw new ValidationException("Informe uma placa válida");
        }

        String normalized = plate.trim().toUpperCase();

        if (!PLATE_PATTERN.matcher(normalized).matches()){
            throw new ValidationException("Informe uma placa válida");
        }

        return normalized;
    }
}
