package com.fret.io.auth_service.service;

import com.fret.io.auth_service.config.PasswordValidator;
import com.fret.io.auth_service.dto.ResetPasswordRequest;
import com.fret.io.auth_service.model.PasswordResets;
import com.fret.io.auth_service.model.User;
import com.fret.io.auth_service.repository.PasswordResetsRepository;
import com.fret.io.auth_service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordResetServiceTest {

    @InjectMocks
    PasswordResetService passwordResetService;

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordResetsRepository passwordResetsRepository;
    @Mock
    EmailService emailService;
    @Mock
    PasswordEncoder encoder;
    @Mock
    PasswordValidator passwordValidator;


    @Test
    void shouldGenerateHashTest(){

        String hashTest = "xyz123";

        String hash1 = passwordResetService.generateResetToken(hashTest);
        String hash2 = passwordResetService.generateResetToken(hashTest);

        assertEquals(hash1, hash2);
    }

    @Test
    void shouldSendEmailWhenMethodIsCalledTest(){
        User user = new User();
        user.setEmail("test@gmail.com");

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        String rawResetTokenTest = UUID.randomUUID().toString();
        String hashedResetTokenTest = "xyz123";

        PasswordResets passwordResets = new PasswordResets();
        passwordResets.setUser(user);
        passwordResets.setTokenHash(hashedResetTokenTest);
        passwordResets.setExpiresAt(LocalDateTime.now().plusHours(1));
        passwordResets.setCreatedAt(LocalDateTime.now());

        passwordResetService.forgotPassword(String.valueOf(passwordResets.getUser().getEmail()));

        assertEquals("test@gmail.com", user.getEmail());

        verify(userRepository).findByEmail(user.getEmail());
        verify(passwordResetsRepository).save(any());
        verify(emailService).sendPasswordReset(eq(user.getEmail()),anyString());
    }

    @Test
    void shouldDoNothingWhenEmailDoesNotExistTest(){

        User user = new User();
        user.setEmail("WrongMail@gmail.com");

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.empty());

        passwordResetService.forgotPassword(user.getEmail());

        verify(userRepository).findByEmail(user.getEmail());
        verify(passwordResetsRepository, never()).save(any());
        verify(emailService, never()).sendPasswordReset(any(), anyString());
    }

    @Test
    void shouldResetPasswordTest(){
        String rawToken = UUID.randomUUID().toString();

        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setNewPassword("newPass123");

        doNothing().when(passwordValidator).validatePassword(anyString());

        String hashTokenReset = passwordResetService.generateResetToken(rawToken);

        User user = new User();
        user.setPasswordHash("oldHashPasword");

        PasswordResets tokenTest = new PasswordResets();
        tokenTest.setUser(user);
        tokenTest.setTokenHash(hashTokenReset);
        tokenTest.setExpiresAt(LocalDateTime.now().plusHours(1));
        tokenTest.setUsedAt(null);

        when(passwordResetsRepository.findByTokenHash(hashTokenReset))
                .thenReturn(tokenTest);

        when(encoder.encode("newPass123"))
                .thenReturn("encodedPassword");

        passwordResetService.resetPassword(rawToken, request);

        assertEquals("encodedPassword", user.getPasswordHash());

        verify(passwordValidator).validatePassword("newPass123");
        verify(passwordResetsRepository).findByTokenHash(hashTokenReset);
        verify(encoder).encode("newPass123");
        verify(userRepository).save(user);
        verify(passwordResetsRepository).save(tokenTest);
    }

    @Test
    void shouldThrowExceptionWhenTokenIsNullTest(){
        String rawToken = UUID.randomUUID().toString();

        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setNewPassword("newPass123");

        doNothing().when(passwordValidator).validatePassword(anyString());

        String hashTokenReset = passwordResetService.generateResetToken(rawToken);

        when(passwordResetsRepository.findByTokenHash(hashTokenReset))
                .thenReturn(null);

        assertThrows(RuntimeException.class, ()->{
            passwordResetService.resetPassword(rawToken,request);
        });

        verify(passwordValidator).validatePassword("newPass123");
        verify(passwordResetsRepository).findByTokenHash(hashTokenReset);
        verify(encoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
        verify(passwordResetsRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenTokenIsExpiredTest(){
        String rawToken = UUID.randomUUID().toString();

        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setNewPassword("newPass123");

        doNothing().when(passwordValidator).validatePassword(anyString());

        String hashResetToken = passwordResetService.generateResetToken(rawToken);

        User user = new User();
        user.setPasswordHash("oldPasswordHash");

        PasswordResets token = new PasswordResets();
        token.setUser(user);
        token.setExpiresAt(LocalDateTime.now().minusHours(1));

        when(passwordResetsRepository.findByTokenHash(hashResetToken))
                .thenReturn(token);

        assertThrows(RuntimeException.class, ()->{
            passwordResetService.resetPassword(rawToken, request);
        });

        verify(passwordValidator).validatePassword("newPass123");
        verify(passwordResetsRepository).findByTokenHash(hashResetToken);
        verify(encoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(passwordResetsRepository, never()).save(any(PasswordResets.class));
    }

    @Test
    void shouldThrowExceptionWhenTokenIsUsedTest(){
        String rawToken = UUID.randomUUID().toString();

        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setNewPassword("newPass123");

        doNothing().when(passwordValidator).validatePassword(anyString());

        String hashResetToken = passwordResetService.generateResetToken(rawToken);

        User user = new User();
        user.setPasswordHash("oldPasswordHash");

        PasswordResets token = new PasswordResets();
        token.setUser(user);
        token.setTokenHash(hashResetToken);
        token.setExpiresAt(LocalDateTime.now().plusHours(1));
        token.setUsedAt(LocalDateTime.now());

        when(passwordResetsRepository.findByTokenHash(hashResetToken))
                .thenReturn(token);

        RuntimeException exception = assertThrows(RuntimeException.class,
                ()-> {
                    passwordResetService.resetPassword(rawToken, request);
                });

        assertEquals("Token ja utilizado", exception.getMessage());

        verify(passwordValidator).validatePassword("newPass123");
        verify(passwordResetsRepository).findByTokenHash(hashResetToken);
        verify(encoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(passwordResetsRepository, never()).save(any(PasswordResets.class));
    }
}
