package com.fret.io.auth_service.repository;

import com.fret.io.auth_service.model.RefreshTokens;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokens, UUID> {

    RefreshTokens findByTokenHash(String hash);

}
