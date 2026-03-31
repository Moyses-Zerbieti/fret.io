package com.fret.io.auth_service.repository;

import com.fret.io.auth_service.model.PasswordResets;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PasswordResetsRepository extends JpaRepository <PasswordResets, UUID> {

    PasswordResets findByTokenHash (String hash);
}
