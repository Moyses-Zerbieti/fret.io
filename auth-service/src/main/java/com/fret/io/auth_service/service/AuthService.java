package com.fret.io.auth_service.service;

import com.fret.io.auth_service.dto.AuthResponse;
import com.fret.io.auth_service.dto.LoginRequest;
import com.fret.io.auth_service.model.User;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(
            UserService userService,
            JwtService jwtService,
            RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public AuthResponse login(LoginRequest loginRequest){
        User user = userService.loginUser(loginRequest);
        String acessToken = jwtService.generateToken(user);
        String refreshToken  = refreshTokenService.generateRefreshToken(user, "Postman");

        return new AuthResponse(acessToken, refreshToken);
    }
}
