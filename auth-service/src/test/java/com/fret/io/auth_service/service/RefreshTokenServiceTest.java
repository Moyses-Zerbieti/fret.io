package com.fret.io.auth_service.service;

import com.fret.io.auth_service.dto.AuthResponse;
import com.fret.io.auth_service.dto.JwtProperties;
import com.fret.io.auth_service.model.RefreshTokens;
import com.fret.io.auth_service.model.Role;
import com.fret.io.auth_service.model.User;
import com.fret.io.auth_service.repository.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {


    @InjectMocks
    RefreshTokenService refreshTokenService;

    @Mock
    RefreshTokenRepository refreshTokenRepository;
    @Mock
    JwtService jwtService;
    @Mock
    JwtProperties jwtProperties;

    @Test
    void shouldGenerateHashTest(){

        String hashTest = "xyz123";

        String hashTested1 = refreshTokenService.generateHash(hashTest);
        String hashTested2 = refreshTokenService.generateHash(hashTest);

        assertEquals(hashTested1, hashTested2);
    }

    @Test
    void shouldGenerateRefreshTokenTest(){
        String deviceInfoTest = "Test";

        UUID userId= UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setRole(Role.MOTORISTA);

        when(jwtProperties.getRefreshTokenDays()).thenReturn(30L);

        String rawToken = refreshTokenService.generateRefreshToken(user,deviceInfoTest);

       assertNotNull(rawToken);

        ArgumentCaptor<RefreshTokens> captor =
                ArgumentCaptor.forClass(RefreshTokens.class);

        verify(refreshTokenRepository).save(captor.capture());

        RefreshTokens savedTokens = captor.getValue();

        assertEquals(user,savedTokens.getUser());
        assertEquals(deviceInfoTest, savedTokens.getDeviceInfo());
        assertNotNull(savedTokens.getTokenHash());
        assertNotNull(savedTokens.getExpiresAt());
    }

    @Test
    void shouldThrowExceptionWithInvalidDeviceInfoTest(){
        String deviceInfoTest = "Wrong Device Info";

        UUID userId= UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setRole(Role.MOTORISTA);

        when(jwtProperties.getRefreshTokenDays()).thenReturn(30L);

        when(refreshTokenRepository.save(any()))
                .thenReturn(new RuntimeException("Error"));

        assertThrows(RuntimeException.class, ()->{
            refreshTokenService.generateRefreshToken(user, deviceInfoTest);
        });

    }

    @Test
    void shouldReturnNewAccessTokenAndNewRefreshTokenTest(){
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setEmail("test@gmail.com");
        user.setPasswordHash("hashedPassword");

        String rawToken = "rawToken";

        RefreshTokens token = new RefreshTokens();
        token.setUser(user);
        token.setDeviceInfo("device Info Test");
        token.setExpiresAt(LocalDateTime.now().plusDays(1));
        token.setRevokedAt(null);
        token.setTokenHash(refreshTokenService.generateHash(rawToken));

        when(refreshTokenRepository.findByTokenHash(anyString()))
                .thenReturn(token);

        when(jwtService.generateAccessToken(user))
                .thenReturn("access-token");

        when(jwtProperties.getRefreshTokenDays())
                .thenReturn(30L);

        AuthResponse response = refreshTokenService.refresh(rawToken);

        assertEquals("access-token", response.getAccessToken());
        assertNotNull(response.getRefreshToken());

        verify(refreshTokenRepository, times(2))
                .save(any(RefreshTokens.class));
    }

    @Test
    void shouldThrowExceptionWhenTokenIsNullTest(){

        when(refreshTokenRepository.findByTokenHash(anyString()))
                .thenReturn(null);

        assertThrows(RuntimeException.class,()->{
            refreshTokenService.refresh("token null");
        });

        verify(refreshTokenRepository, never()).save(any());
        verify(jwtService, never()).generateAccessToken(any());
    }

    @Test
    void shouldThrowExceptionWhenTokenIsRevokedTest(){
        RefreshTokens token = new RefreshTokens();
        token.setRevokedAt(LocalDateTime.now());

        when(refreshTokenRepository.findByTokenHash(anyString()))
                .thenReturn(token);

        assertThrows(RuntimeException.class, ()->{
            refreshTokenService.refresh("token");
        });

        verify(refreshTokenRepository, never()).save(any());
        verify(jwtService, never()).generateAccessToken(any());
    }

    @Test
    void shouldThrowExceptionWhenTokenIsExpiredTest(){
        RefreshTokens token = new RefreshTokens();
        token.setExpiresAt(LocalDateTime.now().minusDays(1));

        when(refreshTokenRepository.findByTokenHash(anyString()))
                .thenReturn(token);

        assertThrows(RuntimeException.class, ()->{
            refreshTokenService.refresh("token");
        });

        verify(refreshTokenRepository, never()).save(any());
        verify(jwtService, never()).generateAccessToken(any());
    }
}
