package com.myinvo.inkwise.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiry-ms}")   // 3_600_000  (1 hour)
    private long accessExpiryMs;

    @Value("${jwt.refresh-token-expiry-ms}")  // 604_800_000 (7 days)
    private long refreshExpiryMs;

    private Key signingKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateAccessToken(String userId) {
        return buildToken(userId, accessExpiryMs);
    }

    public String generateRefreshToken(String userId) {
        return buildToken(userId, refreshExpiryMs);
    }

    private String buildToken(String subject, long expiryMs) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiryMs))
                .signWith(signingKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /** Returns userId or throws if invalid/expired */
    public String validateAndExtractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public long getRefreshExpiryMs() { return refreshExpiryMs; }
    public long getAccessExpiryMs()  { return accessExpiryMs;  }
}