package com.fret.io.auth_service.dto;

public class JwtProperties {

    private final long acessTokenMinutes;
    private final long refreshTokenDays;

    public JwtProperties(long acessTokenMinutes, long refreshTokenDays) {
        this.acessTokenMinutes = acessTokenMinutes;
        this.refreshTokenDays = refreshTokenDays;
    }

    public long getAcessTokenMinutes() {
        return acessTokenMinutes;
    }

    public long getRefreshTokenDays() {
        return refreshTokenDays;
    }
}
