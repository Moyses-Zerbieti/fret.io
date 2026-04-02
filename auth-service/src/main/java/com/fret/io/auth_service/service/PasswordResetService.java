package com.fret.io.auth_service.service;

import com.fret.io.auth_service.config.PasswordValidator;
import com.fret.io.auth_service.dto.ResetPasswordRequest;
import com.fret.io.auth_service.model.PasswordResets;
import com.fret.io.auth_service.model.User;
import com.fret.io.auth_service.repository.PasswordResetsRepository;
import com.fret.io.auth_service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetsRepository passwordResetsRepository;
    private final EmailService emailService;
    private final PasswordEncoder encoder;
    private final PasswordValidator validator;

    public PasswordResetService(UserRepository userRepository, PasswordResetsRepository passwordResetsRepository, EmailService emailService, PasswordEncoder encoder, PasswordValidator validator) {
        this.userRepository = userRepository;
        this.passwordResetsRepository = passwordResetsRepository;
        this.emailService = emailService;
        this.encoder = encoder;
        this.validator = validator;
    }

    public String generateResetToken(String hash){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte [] hashReset = digest.digest(hash.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashReset);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar hash");
        }
    }

    public void forgotPassword(String email){
       User user = userRepository.findByEmail(email)
               .orElse(null);

       if (user == null){
           return;
       }

        String rawResetToken = UUID.randomUUID().toString();
        String hashResetPassword = generateResetToken(rawResetToken);

        PasswordResets resets = new PasswordResets();
        resets.setUser(user);
        resets.setTokenHash(hashResetPassword);
        resets.setExpiresAt(LocalDateTime.now().plusHours(1));
        resets.setCreatedAt(LocalDateTime.now());

        passwordResetsRepository.save(resets);
        emailService.sendPasswordReset(user.getEmail(), rawResetToken);
    }

    public void resetPassword(String rawToken, ResetPasswordRequest requestPassword){

        validator.validatePassword(requestPassword.getNewPassword());

        String tokenReset = generateResetToken(rawToken);

        PasswordResets token = passwordResetsRepository.findByTokenHash(tokenReset);
        if (token == null){
            throw new RuntimeException("Token inválido");
        }
        if (token.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Token expirou");
        }
        if (token.getUsedAt() != null){
            throw new RuntimeException("Token ja utilizado");
        }

        User user = token.getUser();

        user.setPasswordHash(encoder.encode(requestPassword.getNewPassword()));
        userRepository.save(user);

        token.setUsedAt(LocalDateTime.now());
        passwordResetsRepository.save(token);
    }
}


