package com.fret.io.auth_service.service;

import com.fret.io.auth_service.dto.AuthResponse;
import com.fret.io.auth_service.dto.LoginRequest;
import com.fret.io.auth_service.model.User;
import com.fret.io.auth_service.repository.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    UserService userService;
    @Mock
    JwtService jwtService;
    @Mock
    RefreshTokenService refreshTokenService;
    @Mock
    RefreshTokenRepository refreshTokenRepository;


    @Test
    void loginSucessTest(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@gmail.com");
        loginRequest.setPassword("123456");

        String userId = String.valueOf(UUID.randomUUID());

        User user = new User();
        user.setId(UUID.fromString(userId));
        user.setEmail("test@gmail.com");
        user.setPasswordHash("hashedPassword");

        String accessToken = "access-token";
        String refreshToken = "refresh-token";

        when(userService.loginUser(loginRequest)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(accessToken);
        when(refreshTokenService.generateRefreshToken(user,"Postman"))
                .thenReturn(refreshToken);

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());

        verify(userService).loginUser(loginRequest);
        verify(refreshTokenRepository).revokeAllByUserId(UUID.fromString(userId));
        verify(jwtService).generateToken(user);
        verify(refreshTokenService).generateRefreshToken(user, "Postman");
    }


    @Test
    void loginWithInvalidCredentialsTest(){
        LoginRequest request = new LoginRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("123456");

        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPasswordHash("hashedPassword");

        when(userService.loginUser(request))
                .thenThrow(new RuntimeException("Invalid Credentials"));

        assertThrows(RuntimeException.class, () ->{
            authService.login(request);
        });

        verify(jwtService, never()).generateToken(any());
        verify(refreshTokenService, never()).generateRefreshToken(any(), any());
        verify(refreshTokenRepository, never()).revokeAllByUserId(any());
    }

    @Test
    void loginGenerateAccessTokenFailTest(){
        LoginRequest request = new LoginRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("123456");

        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setEmail("test@gmail.com");
        user.setPasswordHash("hashedPassword");

        String accessToken = "access-token";

        when(userService.loginUser(request))
                .thenReturn(user);

        when(jwtService.generateToken(user))
                .thenThrow(new RuntimeException("Error to generate your token"));

        assertThrows(RuntimeException.class, ()->{
           authService.login(request);
        });

        verify(userService).loginUser(request);
        verify(refreshTokenRepository).revokeAllByUserId(userId);
        verify(refreshTokenService, never()).generateRefreshToken(any(), any());
    }

    @Test
    void loginGenerateRefreshTokenFailTest(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@gmail.com");
        loginRequest.setPassword("123456");

        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setEmail("test@gmail.com");
        user.setPasswordHash("hashedPassword");

        String accessToken = "access-token";

        when(userService.loginUser(loginRequest))
                .thenReturn(user);

        when(jwtService.generateToken(user))
                .thenReturn("access-token");

        when(refreshTokenService.generateRefreshToken(user, "Postman"))
                .thenThrow(new RuntimeException ("Error to generate your refresh token"));

        assertThrows(RuntimeException.class, ()->{
           authService.login(loginRequest);
        });

        verify(userService).loginUser(loginRequest);
        verify(refreshTokenRepository).revokeAllByUserId(userId);
        verify(jwtService).generateToken(user);
        verify(refreshTokenService).generateRefreshToken(user, "Postman");

    }
}
