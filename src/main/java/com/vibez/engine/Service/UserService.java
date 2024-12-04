package com.vibez.engine.Service;

import org.bson.types.ObjectId;

import com.vibez.engine.Model.User;

public interface  UserService {
    boolean createUser(User newUser);
    String authenticateUser(User existingUser);
    User getUserProfile(String email);
    boolean updateProfile(User user);
    boolean addFriend(ObjectId userId, ObjectId friendId);
    boolean removeFriend(ObjectId userId, ObjectId friendId);
    boolean addGroup(ObjectId userId, ObjectId groupId);
    boolean removeGroup(ObjectId userId, ObjectId groupId);
}
