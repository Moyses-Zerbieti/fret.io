package com.fret.io.driver_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "driver")
@EntityListeners(AuditingEntityListener.class)
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "full_name", length = 255, nullable = false)
    private String fullName;

    @Column(name = "cpf", length = 18, nullable = false, unique = true)
    private String cpf;

    @Column(name = "cnh_number", length = 11, nullable = false, unique = true)
    private String cnhNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "cnh_category",length = 11, nullable = false)
    private CnhCategory cnhCategory;

    @Column(name = "cnh_expires_at", nullable = false)
    private LocalDate cnhExpiresAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability", length = 10, nullable = false)
    private Availability availability;

    @Column(name = "avg_rating", precision = 3, scale = 2, nullable = false)
    private BigDecimal avgRating;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
