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

    public Friendship sendFriendRequest(ObjectId userId, ObjectId friendId) {
        if (friendshipRepo.findByUserIdAndFriendId(userId, friendId) == null) {
            Friendship friendship = new Friendship();
            friendship.setUserId(userId.toHexString());
            friendship.setFriendId(friendId.toHexString());
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
            return friendship;
        }
        return null; 
    }
    

    public Friendship acceptFriendRequest(ObjectId userId, ObjectId friendId) {
        Friendship friendship = friendshipRepo.findByUserIdAndFriendId(userId, friendId);
        if (friendship != null && friendship.getStatus() == "PENDING") {
            friendship.setStatus("ACCEPTED");
            return friendshipRepo.save(friendship);
        }
        return null; 
    }

    public Friendship rejectFriendRequest(ObjectId userId, ObjectId friendId) {
        Friendship friendship = friendshipRepo.findByUserIdAndFriendId(userId, friendId);
        if (friendship != null && friendship.getStatus() == "PENDING") {
            friendship.setStatus("REJECTED");
            return friendshipRepo.save(friendship);
        }
        return null; 
    }

    public Friendship blockFriend(ObjectId userId, ObjectId friendId) {
        Friendship friendship = friendshipRepo.findByUserIdAndFriendId(userId, friendId);
        if (friendship != null && friendship.getStatus() == "ACCEPTED") {
            friendship.setStatus("BLOCKED");
            return friendshipRepo.save(friendship);
        }
        return null;
    }

    public List<Friendship> getFriends(ObjectId userId) {
        return friendshipRepo.findByUserIdAndStatus(userId, "ACCEPTED");
    }

    public List<Friendship> getPendingRequests(ObjectId userId) {
        return friendshipRepo.findByUserIdAndStatus(userId, "PENDING");
    }
}
