package com.fret.io.driver_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driverId;

    @Column(name = "plate", length = 7, nullable = false, unique = true)
    private String plate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_vehicle", nullable = false)
    private TypeVehicle typeVehicle;

    @Column(name = "brand",length = 254, nullable = false)
    private String brand;

    @Column(name = "model", length = 254, nullable = false)
    private String model;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "capacity_kg", precision = 10, scale = 2, nullable = false)
    private BigDecimal capacityKg;

    @Column(name = "capacity_m3", precision = 10, scale = 3, nullable = false)
    private BigDecimal capacityM3;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_vehicle", nullable = false)
    private StatusVehicle statusVehicle;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at")
    private LocalDateTime createdAt;


}
