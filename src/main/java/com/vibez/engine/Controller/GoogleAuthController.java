package com.vibez.engine.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibez.engine.Model.User;
import com.vibez.engine.Service.GoogleAuthService;

@CrossOrigin(origins = "*" , allowedHeaders = "*")
@RestController
@RequestMapping("/vibez")
public class GoogleAuthController {
    @Autowired
    private GoogleAuthService googleAuthService;

    @PostMapping("/GoogleAuth")
    public ResponseEntity<String> GoogleAuth(@RequestBody User request) {
        String email = request.getEmail();
        String name = request.getUserName();
        String picture = request.getProfilePicture();
        String password = request.getPassword();
        return ResponseEntity.ok(googleAuthService.googleAuthenticator(email, password, name, picture));
    }
    
}
