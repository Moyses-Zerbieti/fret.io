package com.fret.io.auth_service.service;

import com.fret.io.auth_service.dto.AuthResponse;
import com.fret.io.auth_service.dto.LoginRequest;
import com.fret.io.auth_service.model.RefreshTokens;
import com.fret.io.auth_service.model.User;
import com.fret.io.auth_service.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(
            UserService userService,
            JwtService jwtService,
            RefreshTokenService refreshTokenService, RefreshTokenRepository refreshTokenRepository) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public AuthResponse login(LoginRequest loginRequest){
        User user = userService.loginUser(loginRequest);

        refreshTokenRepository.revokeAllByUserId(user.getId());

        String acessToken = jwtService.generateAccessToken(user);
        String refreshToken  = refreshTokenService.generateRefreshToken(user, "Postman");

        return new AuthResponse(acessToken, refreshToken);
    }

    @Transactional
    public void logout(UUID userId){
        List <RefreshTokens> tokens =
                refreshTokenRepository.findByUserIdAndRevokedAtIsNull(userId);

        LocalDateTime now = LocalDateTime.now();

        for (RefreshTokens token : tokens){
            token.setRevokedAt(now);
        }
        refreshTokenRepository.saveAll(tokens);
    }
}
