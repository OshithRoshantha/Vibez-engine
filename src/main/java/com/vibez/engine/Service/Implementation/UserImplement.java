package com.vibez.engine.Service.Implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.Message;
import com.vibez.engine.Model.User;
import com.vibez.engine.Repository.UserRepo;
import com.vibez.engine.Service.FriendshipService;
import com.vibez.engine.Service.JwtService;
import com.vibez.engine.Service.UserService;

@Service
public class UserImplement implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private FriendshipService FriendshipService;

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
        String vibezShip = FriendshipService.sendFriendRequest(newUser.getUserId(), "679b99e09b263c53d878d30f");
        FriendshipService.acceptFriendRequest(vibezShip);
        greetingMessage(newUser.getUserId(), "🎉 Hey there! Welcome to VIBEZ! 🎊 We're so happy to have you here! 💜");
        greetingMessage(newUser.getUserId(), "Now that you've joined, let me give you a quick tour! 🚀");
        greetingMessage(newUser.getUserId(), "🔎 You can connect with others by searching for their username or email. Send them a request, and once they accept, you're all set to chat! 💬✨");
        greetingMessage(newUser.getUserId(), "💡 Want to create a group chat? You totally can! Bring your friends together and vibe out in your own space! 🔥👥");
        greetingMessage(newUser.getUserId(), "🛍️ Oh, and did I mention the Marketplace? You can list items for sale—privately for friends or publicly for everyone to see! Buyers can message you directly, and you can search for cool deals too! 💰🛒");
        greetingMessage(newUser.getUserId(), "🌙 Prefer a dark mode? Check out the settings and switch up the vibe! 😎🌌");
        greetingMessage(newUser.getUserId(), "That's it for now! 🎯 Start exploring, make connections, and enjoy your time on VIBEZ! 🚀💜");
        return true;
    }

    public void greetingMessage(String userId, String messageText) {
        Message message = new Message();
        message.setSenderId("679b99e09b263c53d878d30f");
        message.setReceiverId(userId);
        message.setMessage(messageText);
        message.setTimestamp(java.time.LocalDateTime.now());
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
