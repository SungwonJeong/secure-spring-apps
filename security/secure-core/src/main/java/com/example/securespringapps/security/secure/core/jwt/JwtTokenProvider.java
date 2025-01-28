package com.example.securespringapps.security.secure.core.jwt;

import com.example.securespringapps.security.secure.core.config.JwtAccessTokenKey;
import com.example.securespringapps.security.secure.core.config.JwtRefreshTokenKey;
import com.example.securespringapps.security.secure.core.config.properties.JwtProperties;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String REFRESH_PREFIX = "refresh:";
    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_ROLE = "role";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    private final Key accessTokenKey;
    private final Key refreshTokenKey;
    private final JwtProperties jwtProperties;

    public JwtTokenProvider(
            @JwtAccessTokenKey Key accessTokenKey,
            @JwtRefreshTokenKey Key refreshTokenKey,
            JwtProperties jwtProperties
    ) {
        this.accessTokenKey = accessTokenKey;
        this.refreshTokenKey = refreshTokenKey;
        this.jwtProperties = jwtProperties;
    }

    public String createAccessToken(String email, String role) {
        return generateToken(
                email,
                role,
                jwtProperties.getAccess().getValidity().getSeconds(),
                accessTokenKey,
                BEARER_PREFIX,
                true
        );
    }

    public String createRefreshToken(String email) {
        return generateToken(
                email,
                null,
                jwtProperties.getRefresh().getValidity().getSeconds(),
                refreshTokenKey,
                REFRESH_PREFIX,
                false
        );
    }

    private String generateToken(String email, String role, long validityInSeconds, Key key, String prefix, boolean includeClaims) {
        Date now = new Date();
        Date expiration = calculateExpiration(now, validityInSeconds);

        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SIGNATURE_ALGORITHM);

        if (includeClaims) {
            jwtBuilder
                    .claim(CLAIM_EMAIL, email)
                    .claim(CLAIM_ROLE, role);
        }

        return prefix + jwtBuilder.compact();
    }

    private Date calculateExpiration(Date now, long validityInSeconds) {
        return new Date(now.getTime() + (validityInSeconds * 1000L));
    }
}
