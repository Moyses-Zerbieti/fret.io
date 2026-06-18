package com.fret.io.api_gateway.api_gateway.config;

import com.fret.io.api_gateway.api_gateway.roles.Roles;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class JwtRelayFilter implements GlobalFilter, Ordered {

    private static final Map<String, List<Roles>> ROUTE_PERMISSIONS = Map.of(
            "/driver", List.of(Roles.MOTORISTA),
            "/company", List.of(Roles.EMBARCADORA)
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (path.startsWith("/auth")) {
            return chain.filter(exchange);
        }

        return exchange.getPrincipal()
                .cast(JwtAuthenticationToken.class)
                .flatMap(auth -> {
                    Jwt jwt = auth.getToken();

                    String userId = jwt.getSubject();
                    String role = jwt.getClaimAsString("role");

                    Roles userRole = Roles.valueOf(role);

                    for (Map.Entry<String, List<Roles>> entry : ROUTE_PERMISSIONS.entrySet()){

                        if (path.startsWith(entry.getKey())
                            && !entry.getValue().contains(userRole)){
                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);

                            return exchange.getResponse().setComplete();
                        }
                    }

                    ServerHttpRequest mutatedRequest = exchange.getRequest()
                            .mutate()
                            .header("X-User-Id", userId != null ? userId : "")
                            .header("X-User-Role", role != null ? role : "")
                            .build();

                    return chain.filter(
                            exchange.mutate().request(mutatedRequest).build()
                    );
                })
                .switchIfEmpty(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
