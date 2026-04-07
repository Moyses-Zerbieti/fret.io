package com.fret.io.auth_service.repository;

import com.fret.io.auth_service.model.RefreshTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokens, UUID> {

    RefreshTokens findByTokenHash(String hash);

    @Modifying
    @Query("""
            UPDATE RefreshTokens t
            SET t.revokedAt = CURRENT_TIMESTAMP
            WHERE t.user.id = :userId
            AND t.revokedAt IS NULL
            """)
    void revokeAllByUserId(UUID userId);
}
