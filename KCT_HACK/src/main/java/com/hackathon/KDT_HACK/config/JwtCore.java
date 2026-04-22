package com.hackathon.KDT_HACK.config;

import com.hackathon.KDT_HACK.service.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtCore {

    @Value("${jwt.secret.key}")
    private String secretJwtKey;

    @Value("${jwt.lifetime}")
    private int lifetime;

    protected SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretJwtKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .subject(userDetails.getId())
                .claim("role", userDetails.getRole().name())
                .claim("tv", userDetails.getTokenVersion())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + lifetime))
                .signWith(getSigningKey())
                .compact();
    }

    public String getIdFromJwt(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    @PostConstruct
    public void validateKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretJwtKey);
        if (keyBytes.length < 32) {
            throw new IllegalStateException("JWT secret key must be at least 256 bits");
        }
    }
}
