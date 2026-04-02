package com.fret.io.auth_service.config;

import com.fret.io.auth_service.exception.PasswordInvalidException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PasswordValidator {

    public void validatePassword(String password){

        List<String> errors = new ArrayList<>();

        if (password.length()<8){
            errors.add("Senha deve ter no mínimo 8 caracteres");
        }
        if (!password.matches(".*[A-Z].*")){
            errors.add("Senha deve conter pelo menos uma letra maiúscula");
        }
        if (!password.matches(".*[a-z].*")){
            errors.add("Senha deve conter pelo menos uma letra minúscula");
        }
        if (!password.matches(".*\\d.*")){
            errors.add("Senha deve conter pelo menos um número");
        }
        if (!password.matches(".*[!@#$%&*?_\\-+.].*")){
            errors.add("Senha deve conter pelo menos um caractere especial");
        }
        if (!errors.isEmpty()){
            throw new PasswordInvalidException(errors);
        }
    }
}
