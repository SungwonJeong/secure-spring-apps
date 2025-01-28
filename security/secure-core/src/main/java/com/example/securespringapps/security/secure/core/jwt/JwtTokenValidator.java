package com.example.securespringapps.security.secure.core.jwt;

import com.example.securespringapps.security.secure.core.config.JwtAccessTokenKey;
import com.example.securespringapps.security.secure.core.config.JwtRefreshTokenKey;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;

@Slf4j
@Component
public class JwtTokenValidator {

    private final Key accessTokenKey;
    private final Key refreshTokenKey;

    public JwtTokenValidator(
            @JwtAccessTokenKey Key accessTokenKey,
            @JwtRefreshTokenKey Key refreshTokenKey
    ) {
        this.accessTokenKey = accessTokenKey;
        this.refreshTokenKey = refreshTokenKey;
    }

    public boolean validateToken(String token, boolean isAccess) {
        Key key = isAccess ? accessTokenKey : refreshTokenKey;
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            handleSignatureException(isAccess, e);
        } catch (ExpiredJwtException e) {
            handleExpiredException(isAccess, e);
        } catch (UnsupportedJwtException e) {
            handleUnsupportedException(isAccess, e);
        } catch (MalformedJwtException e) {
            handleMalformedException(isAccess, e);
        } catch (IllegalArgumentException e) {
            handleIllegalArgumentException(isAccess, e);
        }
        return false;
    }

    private void handleSignatureException(boolean isAccess, SignatureException e) {
        String tokenType = isAccess ? "Access Token" : "Refresh Token";
        log.debug("Invalid JWT signature for {}: {}", tokenType, e.getMessage(), e);
        throw new SignatureException(String.format("The %s has an invalid signature. Please check the token integrity.", tokenType));
    }

    private void handleExpiredException(boolean isAccess, ExpiredJwtException e) {
        String tokenType = isAccess ? "Access Token" : "Refresh Token";
        log.debug("Expired JWT token for {}: {}", tokenType, e.getMessage(), e);
        throw new ExpiredJwtException(
                e.getHeader(),
                e.getClaims(),
                String.format("The %s has expired. Please request a new token.", tokenType)
        );
    }

    private void handleUnsupportedException(boolean isAccess, UnsupportedJwtException e) {
        String tokenType = isAccess ? "Access Token" : "Refresh Token";
        log.debug("Unsupported JWT token for {}: {}", tokenType, e.getMessage(), e);
        throw new UnsupportedJwtException(String.format("The %s is unsupported. Verify the token format and algorithm.", tokenType));
    }

    private void handleMalformedException(boolean isAccess, MalformedJwtException e) {
        String tokenType = isAccess ? "Access Token" : "Refresh Token";
        log.debug("Malformed JWT token for {}: {}", tokenType, e.getMessage(), e);
        throw new MalformedJwtException(String.format("The %s is malformed. Ensure the token structure is correct.", tokenType));
    }

    private void handleIllegalArgumentException(boolean isAccess, IllegalArgumentException e) {
        String tokenType = isAccess ? "Access Token" : "Refresh Token";
        log.debug("Invalid argument for {}: {}", tokenType, e.getMessage(), e);
        throw new IllegalArgumentException(String.format("The %s contains invalid arguments. Please check the token value.", tokenType));
    }
}
