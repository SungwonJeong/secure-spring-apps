package com.example.securespringapps.security.secure.core.config;

import com.example.securespringapps.security.secure.core.config.properties.JwtProperties;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;
import java.util.Base64;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtKeyConfig {

    @Bean
    @JwtAccessTokenKey
    public Key accessTokenKey(JwtProperties jwtProperties) {
        byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.getSecret().getKey().getAccess());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Bean
    @JwtRefreshTokenKey
    public Key refreshTokenKey(JwtProperties jwtProperties) {
        byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.getSecret().getKey().getRefresh());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
