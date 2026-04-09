package com.hackathon.KDT_HACK.JWT;

import com.hackathon.KDT_HACK.Registration.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtCore {

    @Value("${jwt.secret.key}")
    private String secretJwtKey;

    @Value("${jwt.lifetime}")
    private int lifetime;

    public String generateToken(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userDetails.getId().toString()))
                .claim("role", userDetails.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime()+ lifetime))
                .signWith(SignatureAlgorithm.HS256, secretJwtKey)
                .compact();
    }
    public String getIdFromJwt(String token){
        String subject = Jwts.parser()
                .setSigningKey(secretJwtKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return subject;

    }
}
