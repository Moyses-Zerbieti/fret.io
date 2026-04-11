package com.fret.io.api_gateway.api_gateway.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import reactor.core.publisher.Mono;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public ReactiveJwtDecoder jwtDecoder(){
        return token -> Mono.just(
                org.springframework.security.oauth2.jwt.Jwt.withTokenValue(token)
                        .header("alg", "none")
                        .claim("sub", "test-user")
                        .build()
        );
    }
}
