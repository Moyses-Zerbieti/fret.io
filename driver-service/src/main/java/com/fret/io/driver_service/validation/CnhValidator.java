package com.fret.io.driver_service.validation;

import jakarta.validation.ValidationException;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;


public final class CnhValidator {

    private static final Pattern CNH_PATTERN = Pattern.compile("^\\d{11}$");

    private CnhValidator(){}

    public static String validateAndNormalize(String cnhNumber){
        if (!StringUtils.hasText(cnhNumber)){
            throw new ValidationException("O número da CNH é obrigatório");
        }
        String cnhNormalized = cnhNumber.trim();

        if (!CNH_PATTERN.matcher(cnhNormalized).matches()){
            throw new ValidationException("O número da CNH deve conter exatamente 11 dígitos numéricos");
        }

        return cnhNormalized;
    }
}
