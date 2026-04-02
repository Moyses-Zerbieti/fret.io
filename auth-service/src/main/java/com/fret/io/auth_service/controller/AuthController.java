package com.fret.io.auth_service.controller;

import com.fret.io.auth_service.dto.*;
import com.fret.io.auth_service.exception.DocInvalidException;
import com.fret.io.auth_service.service.AuthService;
import com.fret.io.auth_service.service.PasswordResetService;
import com.fret.io.auth_service.service.RefreshTokenService;
import com.fret.io.auth_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetService passwordResetService;

    public AuthController(UserService service, AuthService authService, RefreshTokenService refreshTokenService, PasswordResetService passwordResetService){
        this.userService = service;
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/register")
    public ResponseEntity<?>registeredUser(@Valid @RequestBody RegisterRequest request){
            userService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest loginRequest){
            AuthResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request){

        AuthResponse response = refreshTokenService.refresh(
                request.getRefreshToken()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request){
        passwordResetService.forgotPassword(request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<String> resetPassword(
            @PathVariable String token, @RequestBody ResetPasswordRequest request){

        passwordResetService.resetPassword(token,request);
        return ResponseEntity.ok("Senha redefinida com sucesso");
    }
}
