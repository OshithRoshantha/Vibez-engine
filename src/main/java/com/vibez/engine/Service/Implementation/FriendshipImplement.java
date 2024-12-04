package com.vibez.engine.Service.Implementation;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
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

    public String sendFriendRequest(ObjectId userId, ObjectId friendId) {
        if (friendshipRepo.findByUserIdAndFriendId(userId, friendId) == null && friendshipRepo.findByUserIdAndFriendId(friendId, userId) == null) {
            Friendship friendship = new Friendship();
            friendship.setUserId(userId);
            friendship.setFriendId(friendId);
            friendship.setStatus("PENDING");
    
            friendship = friendshipRepo.save(friendship);
            ObjectId friendshipId = friendship.getFriendshipId();
    
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
            return "Friend request sent";	
        }
        return "Not Allowed"; 
    }
    

    public String acceptFriendRequest(ObjectId userId, ObjectId friendId) {
        Friendship friendship = friendshipRepo.findByUserIdAndFriendId(userId, friendId);
        if (friendship == null) {
            friendship = friendshipRepo.findByUserIdAndFriendId(friendId, userId);
        }
        if ("PENDING".equals(friendship.getStatus())) {
            friendship.setStatus("ACCEPTED");
            friendshipRepo.save(friendship);
            return "Friend request accepted";
        }
        return "Not Allowed"; 
    }

    public String rejectFriendRequest(ObjectId userId, ObjectId friendId) {
        Friendship friendship = friendshipRepo.findByUserIdAndFriendId(userId, friendId);
        if (friendship == null) {
            friendship = friendshipRepo.findByUserIdAndFriendId(friendId, userId);
        }
        if ("PENDING".equals(friendship.getStatus())) {
            friendship.setStatus("REJECTED");
            friendshipRepo.save(friendship);
            return "Friend request rejected";
        }
        return "Not Allowed"; 
    }

    public String blockFriend(ObjectId userId, ObjectId friendId) {
        Friendship friendship = friendshipRepo.findByUserIdAndFriendId(userId, friendId);
        if (friendship == null) {
            friendship = friendshipRepo.findByUserIdAndFriendId(friendId, userId);
        }
        if ("ACCEPTED".equals(friendship.getStatus())) {
            friendship.setStatus("BLOCKED");
            friendshipRepo.save(friendship);
            return "Friend blocked";
        }
        return "Not Allowed";
    }

    public List<Friendship> getFriends(ObjectId userId) {
        return friendshipRepo.findByUserIdAndStatus(userId, "ACCEPTED");
    }

    public List<Friendship> getPendingRequests(ObjectId userId) {
        return friendshipRepo.findByUserIdAndStatus(userId, "PENDING");
    }
}
