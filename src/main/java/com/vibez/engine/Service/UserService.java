package com.vibez.engine.Service;
import com.vibez.engine.Model.User;

public interface  UserService {
    boolean createUser(User newUser);
    String authenticateUser(User existingUser);
    User getUserProfile(String email);
    boolean updateProfile(User user);
    User getUserById(String userId);
    void changeDarkMode(boolean darkMode, String userId);
    boolean isUserExist(String email);
}
