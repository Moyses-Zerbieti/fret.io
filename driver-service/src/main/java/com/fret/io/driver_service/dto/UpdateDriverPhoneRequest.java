package com.fret.io.driver_service.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateDriverPhoneRequest {

    @NotBlank(message = "É obrigatório preencher esse campo para concluir")
    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
