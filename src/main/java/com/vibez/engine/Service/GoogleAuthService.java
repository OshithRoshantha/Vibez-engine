package com.vibez.engine.Service;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.User;
import com.vibez.engine.Repository.UserRepo;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;

@Service
public class GoogleAuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    private static final String GOOGLE_PUBLIC_KEY_URL = "https://www.googleapis.com/oauth2/v3/certs";

    public Claims convertToClaims(JWTClaimsSet jwtClaimsSet) {
        Map<String, Object> claims = jwtClaimsSet.getClaims();
        Claims claimsJjwt = new DefaultClaims();
        claimsJjwt.putAll(claims);
        return claimsJjwt;
    }
    public Claims decodeToken(String token) {
        try {
            String kid = (String) Jwts.parserBuilder()
                    .build()
                    .parseClaimsJws(token)
                    .getHeader()
                    .get("kid");

            JWKSet jwkSet = JWKSet.load(new URL(GOOGLE_PUBLIC_KEY_URL));

            JWK jwk = jwkSet.getKeyByKeyId(kid);
            if (jwk == null) {
                throw new RuntimeException("No matching key found in the JWKS for kid: " + kid);
            }

            RSAKey rsaKey = (RSAKey) jwk;
            RSAPublicKey publicKey = rsaKey.toRSAPublicKey();

            SignedJWT signedJWT = SignedJWT.parse(token);
            if (!signedJWT.verify(new RSASSAVerifier(publicKey))) {
                throw new RuntimeException("Invalid signature on JWT token.");
            }

            return convertToClaims(signedJWT.getJWTClaimsSet());
        } catch (Exception e) {
            throw new RuntimeException("Error decoding JWT token", e);
        }
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
        } else {
            return userService.authenticateUser(user);
        }
    }
}
