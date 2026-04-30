package com.fret.io.auth_service.service;

import com.fret.io.auth_service.config.PasswordValidator;
import com.fret.io.auth_service.dto.LoginRequest;
import com.fret.io.auth_service.dto.RegisterRequest;
import com.fret.io.auth_service.exception.DocInvalidException;
import com.fret.io.auth_service.exception.UserInactiveException;
import com.fret.io.auth_service.model.DocumentType;
import com.fret.io.auth_service.model.Role;
import com.fret.io.auth_service.model.User;
import com.fret.io.auth_service.model.UserStatus;
import com.fret.io.auth_service.repository.RefreshTokenRepository;
import com.fret.io.auth_service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder encoder;
    @Mock
    PasswordValidator passwordValidator;
    @Mock
    UserEventPublisher eventPublisher;
    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @Test
    void shouldRegisterUserAsMotoristaWhenCpfIsValidTest(){
        RegisterRequest request = new RegisterRequest();
        request.setDocument("123.456.789-01");
        request.setPassword("123456");
        request.setEmail("email@test.com");

        when(encoder.encode("123456")).thenReturn("hashedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        doNothing().when(passwordValidator).validatePassword(any());

        userService.registerUser(request);

        verify(passwordValidator).validatePassword("123456");

        verify(userRepository).save(argThat(user ->
                user.getRole() == Role.MOTORISTA &&
                user.getEmail().equals("email@test.com")&&
                user.getPasswordHash().equals("hashedPassword")
        ));

        verify(eventPublisher).publishUserRegistered(any(User.class));
    }

    @Test
    void shouldRegisterUserAsEmbarcadoraWhenCnpjIsValidTest(){
        RegisterRequest request = new RegisterRequest();
        request.setDocument("12.345.678/9101-10");
        request.setEmail("test@gmail.com");
        request.setPassword("123456");

        when(encoder.encode("123456")).thenReturn("hashedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        doNothing().when(passwordValidator).validatePassword("123456");

        userService.registerUser(request);

        verify(userRepository).save(argThat(user ->
                user.getRole() == Role.EMBARCADORA &&
                user.getDocumentType() == DocumentType.CNPJ &&
                user.getEmail().equals("test@gmail.com") &&
                user.getPasswordHash().equals("hashedPassword")
        ));

        verify(eventPublisher).publishUserRegistered(any(User.class));
    }

    @Test
    void  shouldThrowExceptionWhenCpfIsInvalidTest(){
        RegisterRequest request = new RegisterRequest();
        request.setDocument("12345678901");
        request.setPassword("123456");

        doNothing().when(passwordValidator).validatePassword(any());

        assertThrows(DocInvalidException.class, () ->{
            userService.registerUser(request);
        });

        verify(userRepository, never()).save(any());
        verify(eventPublisher, never()).publishUserRegistered(any());
    }

    @Test
    void shouldThrowExceptionWhenCnpjIsInvalidTest(){
        RegisterRequest request = new RegisterRequest();
        request.setDocument("12345678901");
        request.setPassword("123456");

        doNothing().when(passwordValidator).validatePassword(any());

        assertThrows(DocInvalidException.class, () ->{
            userService.registerUser(request);
        });

        verify(userRepository, never()).save(any());
        verify(eventPublisher, never()).publishUserRegistered(any());
    }

    @Test
    void shouldReturnUserWhenCredentialsAreValidTest(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@gmail.com");
        loginRequest.setPassword("123456");

        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPasswordHash("hashedPassword");
        user.setUserStatus(UserStatus.ACTIVE);

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(encoder.matches("123456", "hashedPassword"))
                .thenReturn(true);

        User result = userService.loginUser(loginRequest);

        assertEquals(user, result);

        verify(userRepository).findByEmail("test@gmail.com");
        verify(encoder).matches("123456", "hashedPassword");
    }

    @Test
    void  shouldThrowExceptionWhenUserIsBlockedOrSuspendedTest(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@gmail.com");
        loginRequest.setPassword("123456");

        User user  = new User();
        user.setEmail("test@gmail.com");
        user.setPasswordHash("hashedPassord");
        user.setUserStatus(UserStatus.BLOCKED);

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        assertThrows(UserInactiveException.class, ()-> {
            userService.loginUser(loginRequest);

        });

        verify(userRepository).findByEmail("test@gmail.com");;
        verify(encoder, never()).matches(any(), any());
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsInvalidTest(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@gmail.com");
        loginRequest.setPassword("123456");

        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPasswordHash("hashedPassword");
        user.setUserStatus(UserStatus.ACTIVE);

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(encoder.matches("123456", "hashedPassword"))
                .thenReturn(false);

        assertThrows(RuntimeException.class, ()->{
            userService.loginUser(loginRequest);

        });

        verify(userRepository).findByEmail("test@gmail.com");
        verify(encoder).matches("123456", "hashedPassword");
    }

    @Test
    void shouldUpdateStatusAndRevokeTokensWhenUserIsBlockedOrSuspendedTest(){
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setUserStatus(UserStatus.ACTIVE);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        doNothing().when(refreshTokenRepository).revokeAllByUserId(userId);

        userService.updateStatusUser(userId, UserStatus.BLOCKED);

        verify(userRepository).save(argThat(updateUser ->
                updateUser.getUserStatus() == UserStatus.BLOCKED &&
                updateUser.getUpdatedAt() != null

        ));

        verify(refreshTokenRepository).revokeAllByUserId(userId);
        verify(eventPublisher).publishUserDeactivated(userId, "BLOCKED");
    }

    @Test
    void shouldThrowExceptionWhenPublishingEventTest(){
        UUID userId = UUID.randomUUID();

        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@gmail.com");
        request.setDocument("123.456.789-10");

        User usermock = new User();
        usermock.setId(userId);

        when(userRepository.save(any()))
                .thenReturn(usermock);

        doThrow(new RuntimeException("Erro ao publicar evento"))
                .when(eventPublisher)
                .publishUserRegistered(any());

        User result = userService.registerUser(request);

        assertNotNull(result);
    }
}