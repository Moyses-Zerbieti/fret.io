package com.fret.io.auth_service.service;

import com.fret.io.auth_service.dto.AuthResponse;
import com.fret.io.auth_service.dto.JwtProperties;
import com.fret.io.auth_service.model.RefreshTokens;
import com.fret.io.auth_service.model.User;
import com.fret.io.auth_service.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            JwtService jwtService,
            JwtProperties jwtProperties) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
    }

    public String hash(String token){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashed);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar hash", e);
        }

    }

    public String generateRefreshToken(User user, String deviceInfo){
        String rawToken = UUID.randomUUID().toString();
        String hash = hash(rawToken);

        RefreshTokens token = new RefreshTokens();
        token.setUser(user);
        token.setTokenHash(hash);
        token.setDeviceInfo(deviceInfo);
        token.setExpiresAt(
                LocalDateTime.now().plusDays(jwtProperties.getRefreshTokenDays())
        );
        refreshTokenRepository.save(token);

        return rawToken;
    }

    public AuthResponse refresh(String rawToken){
        String hash = hash(rawToken);
        RefreshTokens token = refreshTokenRepository.findByTokenHash(hash);

        if (token == null){
            throw new RuntimeException("Refresh Token Inválido");
        }
        if (token.getRevokedAt() != null){
            throw new RuntimeException("Token Revogado");
        }
        if (token.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Token expirado");
        }
        token.setRevokedAt(LocalDateTime.now());
        refreshTokenRepository.save(token);

        User user = token.getUser();

        String newAcessToken = jwtService.generateToken(user);
        String newRefreshToken = generateRefreshToken(user, token.getDeviceInfo());

        return new AuthResponse(newAcessToken, newRefreshToken);
    }
}
