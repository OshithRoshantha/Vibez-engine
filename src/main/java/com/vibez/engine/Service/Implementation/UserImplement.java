package com.vibez.engine.Service.Implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.User;
import com.vibez.engine.Repository.UserRepo;
import com.vibez.engine.Service.JwtService;
import com.vibez.engine.Service.UserService;

@Service
public class UserImplement implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    
    public boolean createUser(User newUser) {
        User existingUser = userRepo.findByEmail(newUser.getEmail());
        if (existingUser != null) {
            return false;
        }
        String hashedPassword = encoder.encode(newUser.getPassword());
        newUser.setPassword(hashedPassword);
        userRepo.save(newUser);
        return true;
    }

    public String authenticateUser(User existingUser) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(existingUser.getEmail(), existingUser.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(existingUser.getEmail());
        }
        return "Authentication failed";
    }

    public User getUserProfile(String email) {
        return userRepo.findByEmail(email);
    } 
    
    public boolean updateProfile(User user) {
        User existingUser = userRepo.findByEmail(user.getEmail());
        if (existingUser == null) {
            return false;
        }
        existingUser.setUserName(user.getUserName());
        existingUser.setProfilePicture(user.getProfilePicture());
        existingUser.setAbout(user.getAbout());
        userRepo.save(existingUser);
        return true;
    }

    public User getUserById(String userId) {
        return userRepo.findByUserId(userId);
    }

    public void changeDarkMode(boolean darkMode, String userId) {
        User existingUser = userRepo.findByUserId(userId);
        existingUser.setDarkMode(darkMode);
        userRepo.save(existingUser);
    }

    public boolean isUserExist(String email) {
        User existingUser = userRepo.findByEmail(email);
        if (existingUser == null) {
            return false;
        }
        return true;
    }

    public String getPublicKey(String userId) {
        User existingUser = userRepo.findByUserId(userId);
        return existingUser.getPublicKey();
    }

    public List<String> searchAccount(String keyword) {
        List<User> users = userRepo.findByEmailOrUserNameContaining(keyword);
        List<String> result = new ArrayList<>();
        for (User user : users) {
            result.add(user.getUserId());
        }
        return result;
    }
}
