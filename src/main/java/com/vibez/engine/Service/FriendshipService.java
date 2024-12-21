package com.vibez.engine.Service;

import java.util.List;

public interface FriendshipService {
    String sendFriendRequest(String userId, String friendId);
    String acceptFriendRequest(String friendshipId);
    String unFriend(String friendshipId);
    String blockFriend(String friendshipId);
    List<String> getFriends(String userId);
    List<String> getPendingRequests(String userId);
}
