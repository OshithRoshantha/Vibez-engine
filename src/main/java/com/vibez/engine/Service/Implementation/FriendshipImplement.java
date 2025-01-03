package com.vibez.engine.Service.Implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.Friendship;
import com.vibez.engine.Repository.FriendshipRepo;
import com.vibez.engine.Service.FriendshipService;

@Service
public class FriendshipImplement implements FriendshipService {

    @Autowired
    private FriendshipRepo friendshipRepo;

    public String sendFriendRequest(String userId, String friendId) {
        if (friendshipRepo.findByUserIdAndFriendId(userId, friendId) == null && friendshipRepo.findByUserIdAndFriendId(friendId, userId) == null) {
            Friendship friendship = new Friendship();
            friendship.setUserId(userId);
            friendship.setFriendId(friendId);
            friendship.setStatus("PENDING");
            friendship = friendshipRepo.save(friendship);
            String friendshipId = friendship.getFriendshipId();
            return friendshipId;	
        }
        return "Not Allowed"; 
    }
    

    public String acceptFriendRequest(String friendshipId) {
        Friendship friendship = friendshipRepo.findByFriendshipId(friendshipId);
        if ("PENDING".equals(friendship.getStatus())) {
            friendship.setStatus("ACCEPTED");
            friendshipRepo.save(friendship);
            return friendshipId;
        }
        return "Not Allowed"; 
    }

    public String unFriend(String friendshipId) {
        Friendship friendship = friendshipRepo.findByFriendshipId(friendshipId);
        if ("ACCEPTED".equals(friendship.getStatus())) {
            friendship.setStatus("REJECTED");
            friendshipRepo.save(friendship);
            return friendshipId;
        }
        return "Not Allowed"; 
    }

    public String blockFriend(String friendshipId) {
        Friendship friendship = friendshipRepo.findByFriendshipId(friendshipId);
        friendship.setStatus("BLOCKED");
        friendshipRepo.save(friendship);
        return friendshipId;
    }

    public List<String> getFriends(String userId) {
        List <Friendship> friends = friendshipRepo.findByMatchStatusAndUserIdOrFriendId("ACCEPTED", userId);
        List <String> friendIds = new ArrayList<>();
        for (Friendship friend : friends) {
            friendIds.add(friend.getFriendId());
        }
        return friendIds;
    }

    public List<String> getPendingRequests(String userId) {
        List <Friendship> pendingRequests = friendshipRepo.findByMatchStatusAndUserIdOrFriendId("PENDING", userId);
        List <String> pendingRequestIds = new ArrayList<>();
        for (Friendship pendingRequest : pendingRequests) {
            pendingRequestIds.add(pendingRequest.getFriendId());
        }
        return pendingRequestIds;
    }

    public Friendship getFriendshipInfo(String friendshipId) {
        return friendshipRepo.findByFriendshipId(friendshipId);
    }

    public String getFriendshipId(String userId, String friendId){
        Friendship friendship = friendshipRepo.findByUserIdAndFriendId(userId, friendId);
        if (friendship == null) {
            friendship = friendshipRepo.findByUserIdAndFriendId(friendId, userId);
            if (friendship == null){
                return "NOT_FRIENDS";
            }
        }
        return friendship.getFriendshipId();
    }
}
