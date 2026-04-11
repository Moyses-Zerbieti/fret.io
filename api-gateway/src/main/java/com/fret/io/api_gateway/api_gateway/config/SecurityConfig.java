package com.fret.io.api_gateway.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;

import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain webFilterChain(ServerHttpSecurity httpSecurity){
        return httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange-> exchange
                        .pathMatchers("/auth/**").permitAll()
                            .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth-> oauth.jwt(jwt -> {}))
                .build();
    }
}