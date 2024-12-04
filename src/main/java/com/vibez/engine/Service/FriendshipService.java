package com.vibez.engine.Service;

import java.util.List;

import org.bson.types.ObjectId;

import com.vibez.engine.Model.Friendship;

public interface FriendshipService {
    String sendFriendRequest(ObjectId userId, ObjectId friendId);
    Friendship acceptFriendRequest(ObjectId userId, ObjectId friendId);
    Friendship rejectFriendRequest(ObjectId userId, ObjectId friendId);
    Friendship blockFriend(ObjectId userId, ObjectId friendId);
    List<Friendship> getFriends(ObjectId userId);
    List<Friendship> getPendingRequests(ObjectId userId);
}
