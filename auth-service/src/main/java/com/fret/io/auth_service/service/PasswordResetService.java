package com.fret.io.auth_service.service;

import com.fret.io.auth_service.model.PasswordResets;
import com.fret.io.auth_service.model.User;
import com.fret.io.auth_service.repository.PasswordResetsRepository;
import com.fret.io.auth_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetsRepository passwordResetsRepository;

    public PasswordResetService(UserRepository userRepository, PasswordResetsRepository passwordResetsRepository) {
        this.userRepository = userRepository;
        this.passwordResetsRepository = passwordResetsRepository;
    }

    public String resetHash(String hash){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte [] hashReset = digest.digest(hash.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashReset);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar hash");
        }
    }

    public String forgotPassword(String email){
       User user = userRepository.findByEmail(email)
               .orElseThrow(() -> new RuntimeException("Usuário com email " + email + " não encontrado"));

        String rawResetToken = UUID.randomUUID().toString();
        String hashResetPassword = resetHash(rawResetToken);

        PasswordResets resets = new PasswordResets();
        resets.setUser(user);
        resets.setTokenHash(hashResetPassword);
        resets.setExpiresAt(LocalDateTime.now().plusHours(1));
        resets.setCreatedAt(LocalDateTime.now());

        passwordResetsRepository.save(resets);

        return rawResetToken;
    }


}


