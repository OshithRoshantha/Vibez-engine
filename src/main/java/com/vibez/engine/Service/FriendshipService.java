package com.vibez.engine.Service;

import java.util.List;

import com.vibez.engine.Model.Friendship;

public interface FriendshipService {
    String sendFriendRequest(String userId, String friendId);
    String acceptFriendRequest(String userId, String friendId);
    String rejectFriendRequest(String userId, String friendId);
    String blockFriend(String userId, String friendId);
    List<String> getFriends(String userId);
    List<Friendship> getPendingRequests(String userId);
}
