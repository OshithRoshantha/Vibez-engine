package com.vibez.engine.Service.Implementation;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import com.vibez.engine.Model.Friendship;
import com.vibez.engine.Repository.FriendshipRepo;
import com.vibez.engine.Service.FriendshipService;

public class FriendshipImplement implements FriendshipService {

    @Autowired
    private FriendshipRepo friendshipRepo;

    public Friendship sendFriendRequest(ObjectId userId, ObjectId friendId) {
        if (friendshipRepo.findByUserIdAndFriendId(userId, friendId) == null) {
            Friendship friendship = new Friendship();
            friendship.setUserId(userId);
            friendship.setFriendId(friendId);
            friendship.setStatus("PENDING");
            return friendshipRepo.save(friendship);
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
