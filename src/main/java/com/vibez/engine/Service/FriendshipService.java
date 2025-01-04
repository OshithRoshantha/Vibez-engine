package com.vibez.engine.Service;

import java.util.List;

import com.vibez.engine.Model.FriendshipStatus;
import com.vibez.engine.Model.LinkedProfile;

public interface FriendshipService {
    String sendFriendRequest(String userId, String friendId);
    String acceptFriendRequest(String friendshipId);
    String unFriend(String friendshipId);
    String blockFriend(String friendshipId);
    List<String> getFriends(String userId);
    List<String> getPendingRequests(String userId);
    LinkedProfile getFriendshipInfo(String friendshipId, String userId);
    String getFriendshipId(String userId, String friendId);
    boolean checkFriendship(String userId, String friendshipId);
    List<String> getLinkedProfiles(String userId);
    boolean filterPendings(String userId, String friendshipId);
    FriendshipStatus getFriendshipStatus(String friendshipId);
}
