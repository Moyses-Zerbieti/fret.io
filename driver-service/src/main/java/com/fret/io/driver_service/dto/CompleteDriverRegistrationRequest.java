package com.fret.io.driver_service.dto;

import com.fret.io.driver_service.model.CnhCategory;
import com.fret.io.driver_service.validation.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class CompleteDriverRegistrationRequest {

    @NotBlank(message = "O nome completo é obrigatório")
    private String fullName;

    @NotBlank(message = "O telefone é obrigatório")
    @ValidPhoneNumber
    private String phoneNumber;

    @NotBlank(message = "O número da CNH é obrigatório")
    private String cnhNumber;

    @NotNull(message = "A categoria da CNH é obrigatória")
    private CnhCategory cnhCategory;

    @NotNull(message = "A data de validade da CNH é obrigatória")
    private LocalDate cnhExpiresAt;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
}
