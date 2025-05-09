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
    private OnboardChat onboardChat;

    @Autowired
    AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
    
    public boolean createUser(User newUser) {
        User existingUser = userRepo.findByEmail(newUser.getEmail());
        if (existingUser != null) {
            return false;
        }
        String hashedPassword = encoder.encode(newUser.getPassword());
        List<String> directChats = new ArrayList<>();
        List<String> groups = new ArrayList<>();
        newUser.setDirectChatIds(directChats);
        newUser.setGroupIds(groups);
        newUser.setPassword(hashedPassword);
        userRepo.save(newUser);
        onboardChat.sendGreetings(newUser.getUserId());
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
    
    public String updateProfile(User user) {
        User existingUser = userRepo.findByUserId(user.getUserId());
        if (existingUser == null) {
            return null;
        }
        existingUser.setUserName(user.getUserName());
        existingUser.setProfilePicture(user.getProfilePicture());
        existingUser.setAbout(user.getAbout());
        userRepo.save(existingUser);
        return existingUser.getUserId();
    }

    public User getUserById(String userId) {
        return userRepo.findByUserId(userId);
    }

    public void changeDarkMode(boolean darkMode, String userId) {
        User existingUser = userRepo.findByUserId(userId);
        if (existingUser != null) {
            existingUser.setDarkMode(darkMode);
            userRepo.save(existingUser);
        }
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

    public List<String> searchAccount(String keyword, String userId) {
        List<User> users = userRepo.findByEmailOrUserNameStartingWith(keyword);
        List<String> result = new ArrayList<>();
        for (User user : users) {
            if(user.getBlockedUsers() != null){
                if (user.getBlockedUsers().contains(userId)) {
                    continue;
                }
            }
            result.add(user.getUserId());
        }
        return result;
    }

    public List<String> getFavoriteChatsByUser(String userId) {
        User existingUser = userRepo.findByUserId(userId);
        if (existingUser.getFavoriteDirectChats() == null) {
            return new ArrayList<>();
        }
        return existingUser.getFavoriteDirectChats();
    }

    public List<String> getDirectChatsByUser(String userId) {
        User existingUser = userRepo.findByUserId(userId);
        if (existingUser.getDirectChatIds() == null) {
            return new ArrayList<>();
        }
        return existingUser.getDirectChatIds();
    }

    public List<String> getGroupsByUser(String userId) {
        User existingUser = userRepo.findByUserId(userId);
        if (existingUser.getGroupIds() == null) {
            return new ArrayList<>();
        }
        return existingUser.getGroupIds();
    }

    public boolean deleteDirectChats(String userId) {
        User existingUser = userRepo.findByUserId(userId);
        if (existingUser == null) {
            return false;
        }
        existingUser.getDirectChatIds().clear();
        userRepo.save(existingUser);
        return true;
    }

    public boolean deleteGroupChats(String userId) {
        User existingUser = userRepo.findByUserId(userId);
        if (existingUser == null) {
            return false;
        }
        existingUser.getGroupIds().clear();
        userRepo.save(existingUser);
        return true;
    }

    public boolean deleteChat(String userId, String chatId) {
        User existingUser = userRepo.findByUserId(userId);
        if (existingUser == null) {
            return false;
        }
        existingUser.getDirectChatIds().remove(chatId);
        userRepo.save(existingUser);
        return true;
    }
}
