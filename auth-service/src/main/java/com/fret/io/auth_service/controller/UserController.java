package com.fret.io.auth_service.controller;

import com.fret.io.auth_service.dto.RegisterRequest;
import com.fret.io.auth_service.exception.DocInvalidException;
import com.fret.io.auth_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class UserController {

    private final UserService service;

    public UserController(UserService service){
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<?>registeredUser(@Valid @RequestBody RegisterRequest request){
        try{
            service.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        }catch (DocInvalidException e){
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
