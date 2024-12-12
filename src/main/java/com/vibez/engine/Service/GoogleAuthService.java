package com.vibez.engine.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.User;
import com.vibez.engine.Repository.UserRepo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class GoogleAuthService {
    @Value("${jwt.secret.key}")
    private String secretKey;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    public Claims decodeToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())  
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String GoogleAuth(String token) {
        Claims claims = decodeToken(token);
        String email = claims.get("email", String.class);
        String name = claims.get("name", String.class);
        String picture = claims.get("picture", String.class);
        User user = userRepo.findByEmail(email);
        if (user == null) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUserName(name);
            newUser.setProfilePicture(picture);
            userService.createUser(newUser);
            return "User created";
        }
        else{
           return userService.authenticateUser(user);
        }
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
