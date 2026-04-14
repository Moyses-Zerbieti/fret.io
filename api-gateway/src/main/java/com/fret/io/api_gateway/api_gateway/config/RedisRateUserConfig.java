package com.fret.io.api_gateway.api_gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
public class RedisRateUserConfig {

    @Bean
    public KeyResolver userKeyResolver(){
        return exchange ->
                exchange.getPrincipal()
                        .filter(principal -> principal instanceof JwtAuthenticationToken)
                        .cast(JwtAuthenticationToken.class)
                        .map(jwtToken -> jwtToken.getToken().getSubject())
                        .defaultIfEmpty("anonymous");
    }
}
