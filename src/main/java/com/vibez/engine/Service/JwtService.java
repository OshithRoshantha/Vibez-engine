package com.vibez.engine.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    public String generateToken(String email) {
        Map<String,Object> claims = new HashMap<>();	

        return Jwts.builder()
            .claims()
            .add(claims)
            .subject(email)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() * 60 * 60 * 30))
            .and()
            .signWith(getSecretKey())
            .compact();
    }

    private Key getSecretKey() {
        byte[] keyBytes = "your-256-bit-secret".getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
}
