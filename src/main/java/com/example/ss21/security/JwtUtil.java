package com.example.ss21.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET = "medicore-secret-key";
    private final long ACCESS_EXPIRE =
            15 * 60 * 1000;
    private final long REFRESH_EXPIRE =
            7 * 24 * 60 * 60 * 1000;
    public String generateAccessToken(String username) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRE))
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRE))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }
}