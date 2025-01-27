package com.example.securespringapps.security.secure.core.jwt;

import com.example.securespringapps.security.secure.core.config.JwtProperties;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String REFRESH_PREFIX = "refresh:";
    private static final String CLAIM_EMAIL = "email";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    private Key accessTokenKey;
    private Key refreshTokenKey;

    private final JwtProperties jwtProperties;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @PostConstruct
    private void initializeKeys() {
        this.accessTokenKey = createKey(jwtProperties.getSecret().getKey().getAccess());
        this.refreshTokenKey = createKey(jwtProperties.getSecret().getKey().getRefresh());
    }

    public String createAccessToken(String email) {
        return generateToken(
                email,
                jwtProperties.getAccess().getValidity().getSeconds(),
                accessTokenKey,
                BEARER_PREFIX,
                true
        );
    }

    public String createRefreshToken(String email) {
        return generateToken(
                email,
                jwtProperties.getRefresh().getValidity().getSeconds(),
                refreshTokenKey,
                REFRESH_PREFIX,
                false
        );
    }

    private String generateToken(String email, long validityInSeconds, Key key, String prefix, boolean includeClaims) {
        Date now = new Date();
        Date expiration = calculateExpiration(now, validityInSeconds);

        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SIGNATURE_ALGORITHM);

        if (includeClaims) {
            jwtBuilder.claim(CLAIM_EMAIL, email);
        }

        return prefix + jwtBuilder.compact();
    }

    private Key createKey(String base64EncodedKey) {
        byte[] keyBytes = Base64.getDecoder().decode(base64EncodedKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Date calculateExpiration(Date now, long validityInSeconds) {
        return new Date(now.getTime() + (validityInSeconds * 1000L));
    }
}
