package com.vibez.engine.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vibez.engine.Model.User;
import com.vibez.engine.Service.UserService;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody User newUser) {
        userService.createUser(newUser);
        return ResponseEntity.ok("Account Created!");
    }

    @PostMapping("/auth")
    public ResponseEntity<Boolean> authenticateUser(@RequestBody User user) {
        boolean authenticated = userService.authenticateUser(user.getEmail(), user.getPassword());
        return ResponseEntity.ok(authenticated);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@RequestBody User user) {
        User userProfile = userService.getUserProfile(user.getEmail());
        return ResponseEntity.ok(userProfile);
    }
}
