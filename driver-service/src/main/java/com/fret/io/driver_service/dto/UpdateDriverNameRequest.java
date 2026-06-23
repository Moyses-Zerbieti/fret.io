package com.fret.io.driver_service.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateDriverNameRequest {

    @NotBlank(message = "É obrigatório preencher esse campo para concluir")
    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
