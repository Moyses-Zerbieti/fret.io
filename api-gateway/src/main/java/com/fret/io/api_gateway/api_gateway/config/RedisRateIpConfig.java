package com.fret.io.api_gateway.api_gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

@Configuration
public class RedisRateIpConfig {

    @Bean
    @Primary
    public KeyResolver ipKeyResolver(){
        return exchange -> {

            if (exchange.getRequest().getRemoteAddress() == null) {
                return Mono.just("unknown");
            }

            return
            Mono.just((
                    exchange.getRequest()
                        .getRemoteAddress())
                        .getAddress()
                        .getHostAddress()
            );
        };
    }
}
