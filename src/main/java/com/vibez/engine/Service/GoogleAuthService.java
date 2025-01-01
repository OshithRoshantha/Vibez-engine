package com.vibez.engine.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.User;
import com.vibez.engine.Repository.UserRepo;

@Service
public class GoogleAuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    public String googleAuthenticator(String email, String password, String name, String picture) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            User newUser = new User();
            User existingUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setUserName(name);
            newUser.setProfilePicture(picture);
            newUser.setAbout("Hey there! I am using Vibez.");
            userService.createUser(newUser);
            existingUser.setEmail(email);
            existingUser.setPassword(password);

            return userService.authenticateUser(existingUser);
        } 
        else {
            User existingUser = new User();
            existingUser.setEmail(email);
            existingUser.setPassword(password);
            
            return userService.authenticateUser(existingUser);
        }
    }
    
}
