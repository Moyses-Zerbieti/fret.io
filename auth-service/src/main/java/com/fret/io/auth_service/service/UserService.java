package com.fret.io.auth_service.service;

import com.fret.io.auth_service.config.PasswordValidator;
import com.fret.io.auth_service.dto.LoginRequest;
import com.fret.io.auth_service.dto.RegisterRequest;
import com.fret.io.auth_service.exception.DocInvalidException;
import com.fret.io.auth_service.exception.UserInactiveException;
import com.fret.io.auth_service.model.*;
import com.fret.io.auth_service.repository.RefreshTokenRepository;
import com.fret.io.auth_service.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final PasswordValidator validator;
    private final UserEventPublisher eventPublisher;
    private final RefreshTokenRepository refreshTokenRepository;


    public UserService (UserRepository repository,
                        PasswordEncoder encoder, PasswordValidator validator, UserEventPublisher eventPublisher, RefreshTokenRepository refreshTokenRepository){
        this.repository = repository;
        this.encoder = encoder;
        this.validator = validator;
        this.eventPublisher = eventPublisher;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public User registerUser(RegisterRequest userDto){
            String docNumbers = userDto.getDocument();
            validator.validatePassword(userDto.getPassword());

            User user = userDto.toModel();

            if (docNumbers.length() == 14) {
                if (!isValidCPF(docNumbers)) throw new DocInvalidException("CPF inválido");
                user.setDocumentType(DocumentType.CPF);
                user.setRole(Role.MOTORISTA);

            } else if (docNumbers.length() == 18) {
                if (!isValidCNPJ(docNumbers)) throw new DocInvalidException("CNPJ inválido");
                user.setDocumentType(DocumentType.CNPJ);
                user.setRole(Role.EMBARCADORA);
            } else {
                throw new DocInvalidException("Documento inválido");
            }

            user.setUserStatus(UserStatus.ACTIVE);
            user.setPasswordHash(encoder.encode(userDto.getPassword()));

            User savedUser = repository.save(user);

            try{
                eventPublisher.publishUserRegistered(savedUser);
            }catch (Exception e){
                System.out.println("Erro ao publicar evento user.registered: " + e.getMessage());
            }

            return savedUser;
    }

    public User loginUser(LoginRequest loginRequest){
        User user = repository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (user.getUserStatus() == UserStatus.SUSPENDED ||
        user.getUserStatus() == UserStatus.BLOCKED){
            throw new UserInactiveException();
        }

        if (!encoder.matches(loginRequest.getPassword(), user.getPasswordHash())){
            throw  new RuntimeException("Senha inválida");
        }
        return user;
    }

    @Transactional
    public void updateStatusUser(UUID userId, UserStatus status){
        User user = repository.findById(userId)
                .orElseThrow(()-> new RuntimeException("Usuário não encontrado"));

        user.setUserStatus(status);
        user.setUpdatedAt(LocalDateTime.now());

        repository.save(user);
        refreshTokenRepository.revokeAllByUserId(userId);

        if (status == UserStatus.SUSPENDED || status == UserStatus.BLOCKED){
            eventPublisher.publishUserDeactivated(
                    user.getId(),
                    status.name()
            );
        }
    }

    private boolean isValidCPF(String doc){
        return doc.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    }

    private boolean isValidCNPJ(String doc){
        return doc.matches("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}");
    }

}
