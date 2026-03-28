package com.fret.io.auth_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "refresh_tokens")
public class RefreshTokens {

   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   private UUID id;

   @ManyToOne
   @JoinColumn(name = "user_id")
   private User user;

   @Column(name = "token_hash", nullable = false, unique = true, columnDefinition = "TEXT")
   private String tokenHash;

   @Column(name = "device_info", nullable = false, columnDefinition = "TEXT")
   private String deviceInfo;

   @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
   @Column(name = "expires_at")
   private LocalDateTime expiresAt;

   @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
   @Column(name = "revoked_at")
   private LocalDateTime revokedAt;

   @CreatedDate
   @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
   @Column(name = "created_at")
   private LocalDateTime createdAt;

   public UUID getId() {
      return id;
   }

   public void setId(UUID id) {
      this.id = id;
   }

   public User getUser() {
      return user;
   }

   public void setUser(User user) {
      this.user = user;
   }

   public String getTokenHash() {
      return tokenHash;
   }

   public void setTokenHash(String tokenHash) {
      this.tokenHash = tokenHash;
   }

   public String getDeviceInfo() {
      return deviceInfo;
   }

   public void setDeviceInfo(String deviceInfo) {
      this.deviceInfo = deviceInfo;
   }

   public LocalDateTime getExpiresAt() {
      return expiresAt;
   }

   public void setExpiresAt(LocalDateTime expiresAt) {
      this.expiresAt = expiresAt;
   }

   public LocalDateTime getRevokedAt() {
      return revokedAt;
   }

   public void setRevokedAt(LocalDateTime revokedAt) {
      this.revokedAt = revokedAt;
   }

   public LocalDateTime getCreatedAt() {
      return createdAt;
   }

   public void setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
   }

}
