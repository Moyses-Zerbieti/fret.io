package com.fret.io.auth_service.config;

import com.fret.io.auth_service.dto.JwtProperties;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final RSAKey rsaKey;

    public SecurityConfiguration() throws Exception {
        this.rsaKey = generateRSAKey();
    }

    private RSAKey generateRSAKey() throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        RSAPublicKey publicKeyRsa = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKeyRsa = (RSAPrivateKey) keyPair.getPrivate();

        return new RSAKey
                .Builder(publicKeyRsa)
                .privateKey(privateKeyRsa)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/register",
                                "/login",
                                "/refresh",
                                "/forgot-password",
                                "/reset-password/**",
                                "/status/**",
                                "/.well-known/**")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder()))
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(){
        JWKSet jwkSet = new JWKSet(this.rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
     public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource){
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        try {
            return NimbusJwtDecoder
                    .withPublicKey(this.rsaKey.toRSAPublicKey())
                    .build();
        }catch (Exception e){
            throw new JwtDecoderInitializationException("Erro ao inicializar o decoder",e);
        }
    }

    @Bean
    public JwtProperties jwtProperties() {
        return new JwtProperties(15, 30);
    }
}
