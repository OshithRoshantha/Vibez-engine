package com.vibez.engine.Service.Implementation;

import org.bson.types.ObjectId;
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

    public boolean addFriend(String email, String friendEmail) {
        User user = userRepo.findByEmail(email);
        User friend = userRepo.findByEmail(friendEmail);
        if (user == null || friend == null) {
            return false;
        }
        user.getFriendIds().add(friend.getEmail());
        friend.getFriendIds().add(user.getEmail());
        userRepo.save(user);
        userRepo.save(friend);
        return true;
    }

    public boolean removeFriend(String email, String friendEmail) {
        User user = userRepo.findByEmail(email);
        User friend = userRepo.findByEmail(friendEmail);
        if (user == null || friend == null) {
            return false;
        }
        user.getFriendIds().remove(friend.getEmail());
        friend.getFriendIds().remove(user.getEmail());
        userRepo.save(user);
        userRepo.save(friend);
        return true;
    }

    public boolean addGroup(String email, ObjectId groupId) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            return false;
        }
        user.getGroupIds().add(groupId);
        userRepo.save(user);
        return true;
    }

    public boolean removeGroup(String email, ObjectId groupId) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            return false;
        }
        user.getGroupIds().remove(groupId);
        userRepo.save(user);
        return true;
    }
}
