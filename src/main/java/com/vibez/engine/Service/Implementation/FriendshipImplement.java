package com.vibez.engine.Service.Implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.Friendship;
import com.vibez.engine.Model.User;
import com.vibez.engine.Repository.FriendshipRepo;
import com.vibez.engine.Repository.UserRepo;
import com.vibez.engine.Service.FriendshipService;

@Service
public class FriendshipImplement implements FriendshipService {

    @Autowired
    private FriendshipRepo friendshipRepo;

    @Autowired
    private UserRepo userRepo;

    public String sendFriendRequest(String userId, String friendId) {
        if (friendshipRepo.findByUserIdAndFriendId(userId, friendId) == null && friendshipRepo.findByUserIdAndFriendId(friendId, userId) == null) {
            Friendship friendship = new Friendship();
            friendship.setUserId(userId);
            friendship.setFriendId(friendId);
            friendship.setStatus("PENDING");
    
            friendship = friendshipRepo.save(friendship);
            String friendshipId = friendship.getFriendshipId();
    
            User user = userRepo.findByUserId(userId);
            User friend = userRepo.findByUserId(friendId);
    
            if (user.getFriendshipIds() == null) {
                user.setFriendshipIds(new ArrayList<>());
            }

            user.getFriendshipIds().add(friendshipId);
    
            if (friend.getFriendshipIds() == null) {
                friend.setFriendshipIds(new ArrayList<>());
            }

            friend.getFriendshipIds().add(friendshipId);
            userRepo.save(user);
            userRepo.save(friend);
            return friendshipId;	
        }
        return "Not Allowed"; 
    }
    

    public String acceptFriendRequest(String friendshipId) {
        Friendship friendship = friendshipRepo.findByFriendshipId(friendshipId);
        if ("PENDING".equals(friendship.getStatus())) {
            friendship.setStatus("ACCEPTED");
            friendshipRepo.save(friendship);
            return "Friend request accepted";
        }
        return "Not Allowed"; 
    }

    public String rejectFriendRequest(String friendshipId) {
        Friendship friendship = friendshipRepo.findByFriendshipId(friendshipId);
        if ("PENDING".equals(friendship.getStatus())) {
            friendship.setStatus("REJECTED");
            friendshipRepo.save(friendship);
            return "Friend request rejected";
        }
        return "Not Allowed"; 
    }

    public String blockFriend(String friendshipId) {
        Friendship friendship = friendshipRepo.findByFriendshipId(friendshipId);
        if ("ACCEPTED".equals(friendship.getStatus())) {
            friendship.setStatus("BLOCKED");
            friendshipRepo.save(friendship);
            return "Friend blocked";
        }
        return "Not Allowed";
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
}
