package com.fret.io.auth_service.dto;

import jakarta.validation.constraints.NotBlank;

public class RefreshRequest {

    @NotBlank (message = "Informe o token para retomar o acesso ao sistema")
    public String refreshToken;

    public RefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
