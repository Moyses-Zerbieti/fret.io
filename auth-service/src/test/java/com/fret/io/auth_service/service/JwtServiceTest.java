package com.fret.io.auth_service.service;

import com.fret.io.auth_service.dto.JwtProperties;
import com.fret.io.auth_service.model.Role;
import com.fret.io.auth_service.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    JwtService jwtService;

    @Mock
    JwtProperties jwtProperties;
    @Mock
    JwtEncoder jwtEncoder;


    @Test
    void shouldGenerateAccesTokenTest(){
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setEmail("test@gmail.com");
        user.setRole(Role.MOTORISTA);

        when(jwtProperties.getAcessTokenMinutes()).thenReturn(10L);

        var jwtMock = org.mockito.Mockito.mock(Jwt.class);
        when(jwtMock.getTokenValue()).thenReturn("token");

        when(jwtEncoder.encode(org.mockito.Mockito.any()))
                .thenReturn(jwtMock);

        String token = jwtService.generateToken(user);

        assertEquals("token", token);

        verify(jwtEncoder).encode(any());
    }

}
