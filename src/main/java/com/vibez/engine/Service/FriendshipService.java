package com.vibez.engine.Service;

import java.util.List;

import org.bson.types.ObjectId;

import com.vibez.engine.Model.Friendship;

public interface FriendshipService {
    String sendFriendRequest(ObjectId userId, ObjectId friendId);
    String acceptFriendRequest(ObjectId userId, ObjectId friendId);
    String rejectFriendRequest(ObjectId userId, ObjectId friendId);
    String blockFriend(ObjectId userId, ObjectId friendId);
    List<Friendship> getFriends(ObjectId userId);
    List<Friendship> getPendingRequests(ObjectId userId);
}
