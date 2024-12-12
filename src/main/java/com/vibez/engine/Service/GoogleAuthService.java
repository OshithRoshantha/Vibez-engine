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

    public boolean createAccountByGoogle(String email, String password, String name, String picture) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setUserName(name);
            newUser.setProfilePicture(picture);
            userService.createUser(newUser);
            userService.createUser(newUser);
            return true;
        } 
        return false;
    }
    
}
