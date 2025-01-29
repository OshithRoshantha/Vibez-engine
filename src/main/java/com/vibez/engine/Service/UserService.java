package com.vibez.engine.Service;
import java.util.List;

import com.vibez.engine.Model.User;

public interface  UserService {
    List<User> getAllUsers();
    boolean createUser(User newUser);
    String authenticateUser(User existingUser);
    User getUserProfile(String email);
    String updateProfile(User user);
    User getUserById(String userId);
    void changeDarkMode(boolean darkMode, String userId);
    boolean isUserExist(String email);
    String getPublicKey(String userId);
    List<String> searchAccount(String keyword, String userId);
    List<String> getFavoriteChatsByUser(String userId);
    List<String> getDirectChatsByUser(String userId);
    List<String> getGroupsByUser(String userId);
    boolean deleteDirectChats(String userId);
    boolean deleteGroupChats(String userId);
}
