package com.fret.io.driver_service.dto;

import com.fret.io.driver_service.model.CnhCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DriverResponse {

    private String fullName;
    private String cpf;
    private String email;
    private String phoneNumber;
    private String cnhNumber;
    private CnhCategory cnhCategory;
    private LocalDate cnhExpiresAt;
    private BigDecimal avgRating;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCnhNumber() {
        return cnhNumber;
    }

    public void setCnhNumber(String cnhNumber) {
        this.cnhNumber = cnhNumber;
    }

    public CnhCategory getCnhCategory() {
        return cnhCategory;
    }

    public void setCnhCategory(CnhCategory cnhCategory) {
        this.cnhCategory = cnhCategory;
    }

    public LocalDate getCnhExpiresAt() {
        return cnhExpiresAt;
    }

    public void setCnhExpiresAt(LocalDate cnhExpiresAt) {
        this.cnhExpiresAt = cnhExpiresAt;
    }

    public BigDecimal getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(BigDecimal avgRating) {
        this.avgRating = avgRating;
    }
}
