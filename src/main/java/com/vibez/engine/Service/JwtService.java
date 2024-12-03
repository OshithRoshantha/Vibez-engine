package com.vibez.engine.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @SuppressWarnings("deprecation")
    public String generateToken(String email) {
        Map<String,Object> claims = new HashMap<>();	

        return Jwts.builder()
            .setClaims(claims) 
            .setSubject(email) 
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 30))) 
            .signWith(getSecretKey())
            .compact();
    }

    private Key getSecretKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }
    
}
