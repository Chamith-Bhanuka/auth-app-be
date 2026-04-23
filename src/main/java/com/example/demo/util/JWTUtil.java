package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt.secretKey}")
    private String SECRET;

    // short-lived - 10 minutes
    public String generateAccessToken(String email, String role) {
        return createToken(email, role, 1000 * 60 * 10);
    }

    // long-lived - 30 minutes
    public String generateRefreshToken(String email) {
        return createToken(email, null, 1000 * 60 * 30);
    }

    private String createToken(String email, String role, long expiration) {
        var builder = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, SECRET);
        if (role != null) builder.claim("role", role);
        return builder.compact();
    }

    // get email from token
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // get role from token
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // validate token
    public boolean isValid(String token) {
        try {
            return getClaims(token).getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
