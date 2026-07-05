package com.fret.io.driver_service.commom;

import com.fret.io.driver_service.exception.DriverNotFoundException;
import com.fret.io.driver_service.exception.DriverRegistrationAlreadyCompleteException;
import com.fret.io.driver_service.exception.PlateAlreadyExistsException;
import com.fret.io.driver_service.exception.VehicleNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.xml.parsers.SAXParser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException exception){
        List <String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        Map <String, Object> response = new HashMap<>();
        response.put("message", "Erro de validação");
        response.put("errors", errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DriverNotFoundException.class)
    public ResponseEntity<?> driverNotFoundException(DriverNotFoundException exception){
      Map <String, Object> response = new HashMap<>();
      response.put("message", "Erro na busca de motorista");
      response.put("error", exception.getMessage());

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DriverRegistrationAlreadyCompleteException.class)
    public ResponseEntity<?> driverRegistrationAlreadyCompleteException(DriverRegistrationAlreadyCompleteException exception){
        Map <String, Object> response = new HashMap<>();
        response.put("message", "Erro no cadastro de motorista");
        response.put("error", exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(PlateAlreadyExistsException.class)
    public ResponseEntity<?>plateAlreadyExistsException(PlateAlreadyExistsException exception){
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Erro no cadastro do veículo");
        response.put("error", exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> httpMessageNotReadableException(HttpMessageNotReadableException exception){
        Map <String, Object> response = new HashMap<>();
        response.put("message", "Erro de validação");
        response.put("error", "Preencha todos os campos concluir");

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<?>  vehicleNotFoundException(VehicleNotFoundException exception){
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Erro na busca de veículo");
        response.put("error", exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}
