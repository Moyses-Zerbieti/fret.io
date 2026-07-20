package com.fret.io.driver_service.validation.validator;

import com.fret.io.driver_service.validation.annotation.ValidPhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+[1-9]\\d{0,2}\\s\\d{8,15}$");

    @Override
    public boolean isValid(String phoneNumber,
                           ConstraintValidatorContext context) {

        if (phoneNumber == null || phoneNumber.isBlank()){
            return true;
        }

            return PHONE_PATTERN.matcher(phoneNumber).matches();
        }
}
