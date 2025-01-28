package com.vibez.engine.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibez.engine.Model.User;
import com.vibez.engine.Service.JwtService;
import com.vibez.engine.Service.UserService;


@CrossOrigin(origins = "*" , allowedHeaders = "*")
@RestController
@RequestMapping("/vibez")
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
        newUser.setDarkMode(false);
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

    @GetMapping("/profile/{userId}")
    public ResponseEntity<User> getUserById(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/profile/darkmode/{userId}/{mode}")
    public ResponseEntity<Boolean> changeDarkMode(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId, @PathVariable boolean mode) {
        userService.changeDarkMode(mode, userId);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/profile/isExist/{email}")
    public ResponseEntity<Boolean> isUserExist(@PathVariable String email) {
        return ResponseEntity.ok(userService.isUserExist(email));
    }

    @GetMapping("/profile/publicKey/{userId}")
    public ResponseEntity<String> getPublicKey(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId) {
        return ResponseEntity.ok(userService.getPublicKey(userId));
    }

    @GetMapping("/search/{userId}/{keyword}")
    public ResponseEntity<List<String>> searchAccount(@PathVariable String keyword, @PathVariable String userId) {
        return ResponseEntity.ok(userService.searchAccount(keyword, userId));
    }

    @GetMapping("/allChats/{userId}")
    public ResponseEntity<List<String>> getDirectChatsByUser(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId) {
        return ResponseEntity.ok(userService.getDirectChatsByUser(userId));
    }

    @GetMapping("/favoriteChats/{userId}")
    public ResponseEntity<List<String>> getFavoriteChatsByUser(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId){
        return ResponseEntity.ok(userService.getFavoriteChatsByUser(userId));
    }

    @GetMapping("/allGroups/{userId}")
    public ResponseEntity<List<String>> getGroupsByUser(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId){
        return ResponseEntity.ok(userService.getGroupsByUser(userId));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Boolean> deleteUser(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId){
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

    @PutMapping("/delete/directChats/{userId}")
    public ResponseEntity<Boolean> deleteDirectChats(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId){
        return ResponseEntity.ok(userService.deleteDirectChats(userId));
    }

    @PutMapping("/delete/groupChats/{userId}")
    public ResponseEntity<Boolean> deleteGroupChats(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId){
        return ResponseEntity.ok(userService.deleteGroupChats(userId));
    }

}