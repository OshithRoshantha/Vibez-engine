package com.vibez.engine.Service;

import com.vibez.engine.Model.User;

public interface  UserService {
    User createUser(User newUser);
    boolean authenticateUser(String email, String password);
    User getUserProfile(String email);
}
