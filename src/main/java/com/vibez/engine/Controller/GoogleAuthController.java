package com.vibez.engine.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibez.engine.Service.GoogleAuthService;

@RestController
public class GoogleAuthController {
    @Autowired
    private GoogleAuthService googleAuthService;

    @PostMapping("/googleAuth/{googleToken}")
    public ResponseEntity<String> GoogleAuth(@PathVariable String token) {
        return ResponseEntity.ok(googleAuthService.GoogleAuth(token));
    } 
}
