package com.fret.io.driver_service.validation.annotation;

import com.fret.io.driver_service.validation.validator.PhoneNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumber {

    String message() default "Telefone inválido. Informe o número com o código do país. Exemplo: +55 11987654321";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
