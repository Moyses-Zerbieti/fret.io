package com.fret.io.api_gateway.api_gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtRelayFIlter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println("➡ METHOD: " + exchange.getRequest().getMethod());
        System.out.println("➡ PATH: " + exchange.getRequest().getURI().getPath());

        String path = exchange.getRequest().getURI().getPath();

        if(path.startsWith("/auth")){
            return chain.filter(exchange);
        }

        return exchange.getPrincipal()
                .cast(JwtAuthenticationToken.class)
                .map(auth ->{
                    Jwt jwt = auth.getToken();

                    String tokenValue = jwt.getTokenValue();
                    String userId = jwt.getSubject();
                    String role = jwt.getClaim("role");

                    return exchange.getRequest()
                            .mutate()
                            .header("X-User-Id", userId)
                            .header("X-User-Role", role)
                            .build();
                })
                .defaultIfEmpty(exchange.getRequest())
                .flatMap(mutatedRequest -> chain.filter(
                        exchange.mutate().request(mutatedRequest).build()
                ));

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
