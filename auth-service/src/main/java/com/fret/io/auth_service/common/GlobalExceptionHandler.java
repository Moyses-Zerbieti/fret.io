package com.fret.io.auth_service.common;

import com.fret.io.auth_service.exception.DocInvalidException;
import com.fret.io.auth_service.exception.PasswordInvalidException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<?> handlePasswordValidation(PasswordInvalidException exception){
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Erro na validação de senha");
        response.put("errors", exception.getErrors());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException exception){
        List <String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Erro de validação");
        response.put("errors", errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DocInvalidException.class)
    public ResponseEntity<?> docInvalidException(DocInvalidException exception){

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Documento inválido");
        response.put("errors", exception.getErrors());

        return ResponseEntity.badRequest().body(response);
    }
}
