package com.vibez.engine.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.vibez.engine.Model.User;
import com.vibez.engine.Service.JwtService;
import com.vibez.engine.Service.UserService;


@RestController
public class UserController {

    private static final String DEFAULT_PROFILE_PICTURE = "https://media.istockphoto.com/id/1300845620/vector/user-icon-flat-isolated-on-white-background-user-symbol-vector-illustration.jpg?s=612x612&w=0&k=20&c=yBeyba0hUkh14_jgv1OKqIH0CCSWU_4ckRkAoy2p73o=";
    private static final String DEFAULT_AB = "Hey there! I am using Vibez.";

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;
    
    @PostMapping("/register")
    public ResponseEntity<Boolean> createUser(@RequestBody User newUser) {
        if (newUser.getProfilePicture() == "" || newUser.getProfilePicture() == null) { 
            newUser.setProfilePicture(DEFAULT_PROFILE_PICTURE);
        }
        if (newUser.getAbout() == "" || newUser.getAbout() == null) {
            newUser.setAbout(DEFAULT_AB);
        }
        return ResponseEntity.ok(userService.createUser(newUser));
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody User exsistingUser) {
        String accessToken = userService.authenticateUser(exsistingUser);
        return ResponseEntity.ok(accessToken);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader(value = "Authorization", required = true) String token) {
        String jwtToken = token.replace("Bearer ", "");
        String email = jwtService.extractUserEmail(jwtToken); 
        User userProfile = userService.getUserProfile(email);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/profile/update")
    public ResponseEntity<Boolean> updateProfile(@RequestBody User user) {
        return ResponseEntity.ok(userService.updateProfile(user));
    }

}