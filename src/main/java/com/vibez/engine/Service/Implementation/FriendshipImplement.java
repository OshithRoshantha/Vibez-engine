package com.vibez.engine.Service.Implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.Friendship;
import com.vibez.engine.Model.FriendshipStatus;
import com.vibez.engine.Model.LinkedProfile;
import com.vibez.engine.Model.User;
import com.vibez.engine.Repository.FriendshipRepo;
import com.vibez.engine.Service.FriendshipService;
import com.vibez.engine.Service.UserService;

@Service
public class FriendshipImplement implements FriendshipService {

    @Autowired
    private FriendshipRepo friendshipRepo;

    @Autowired
    private UserService userService;

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
            friendshipRepo.deleteByFriendshipId(friendshipId);
            return friendshipId;
        }
        return "Not Allowed"; 
    }

    public String blockFriend(String friendshipId) {
        Friendship friendship = friendshipRepo.findByFriendshipId(friendshipId);
        User user1 = userService.getUserById(friendship.getUserId());
        User user2 = userService.getUserById(friendship.getFriendId());
        if (user1.getBlockedUsers() == null) {
            ArrayList<String> blockedUsers = new ArrayList<>();
            blockedUsers.add(user2.getUserId());
            user1.setBlockedUsers(blockedUsers);
        }
        else{
            user1.getBlockedUsers().add(user2.getUserId());
        }
        if (user2.getBlockedUsers() == null){
            ArrayList<String> blockedUsers = new ArrayList<>();
            blockedUsers.add(user1.getUserId());
            user2.setBlockedUsers(blockedUsers);
        }
        else{
            user2.getBlockedUsers().add(user1.getUserId());
        }
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

    public LinkedProfile getFriendshipInfo(String friendshipId, String userId) {
        Friendship friendship = friendshipRepo.findByFriendshipId(friendshipId);
        String myId = friendship.getUserId();
        String friendId = friendship.getFriendId();
        if (myId.equals(userId)) {
            User friend = userService.getUserById(friendId);
            LinkedProfile linkedProfile = new LinkedProfile();
            linkedProfile.setFriendshipId(friendshipId);
            linkedProfile.setProfileId(friendId);
            linkedProfile.setProfileName(friend.getUserName());
            linkedProfile.setProfilePicture(friend.getProfilePicture());
            linkedProfile.setStatus(friendship.getStatus());
            linkedProfile.setProfileAbout(friend.getAbout());
            return linkedProfile;
        }   
        else{
            User me = userService.getUserById(myId);
            LinkedProfile linkedProfile = new LinkedProfile();
            linkedProfile.setFriendshipId(friendshipId);
            linkedProfile.setProfileId(myId);
            linkedProfile.setProfileName(me.getUserName());
            linkedProfile.setProfilePicture(me.getProfilePicture());
            linkedProfile.setStatus(friendship.getStatus());
            linkedProfile.setProfileAbout(me.getAbout());
            return linkedProfile;
        }
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

    public boolean checkFriendship(String userId, String friendshipId){
        Friendship friendship = friendshipRepo.findByFriendshipId(friendshipId);
        if (friendship == null){
            return false;
        }
        else{
            if (friendship.getUserId().equals(userId) || friendship.getFriendId().equals(userId)){
                return true;
            }
            return false;
        }

    }

    public List<String> getLinkedProfiles(String userId){
        List <Friendship> friends = friendshipRepo.findByUserIdOrFriendId(userId);
        List <String> linkedProfiles = new ArrayList<>();
        if (friends != null){
            for (Friendship friend : friends){
                linkedProfiles.add(friend.getFriendshipId());
            }
        }
        return linkedProfiles;
    }

    public boolean filterPendings(String userId, String friendshipId){
        Friendship friendship = friendshipRepo.findByFriendshipId(friendshipId);
        if (friendship.getUserId().equals(userId) && friendship.getStatus().equals("PENDING")){
            return false;
        }
        return true;
    }

    public boolean filterAccepteds(String userId, String friendshipId){
        Friendship friendship = friendshipRepo.findByFriendshipId(friendshipId);
        if (friendship.getUserId().equals(userId) && friendship.getStatus().equals("ACCEPTED")){
            return true;
        }
        return false;
    }

    public FriendshipStatus getFriendshipStatus(String friendshipId){
        Friendship friendship = friendshipRepo.findByFriendshipId(friendshipId);
        FriendshipStatus status = new FriendshipStatus();
        status.setStatus(friendship.getStatus());
        status.setUserId(friendship.getUserId());
        status.setFriendId(friendship.getFriendId());
        return status;
    }

    public List<LinkedProfile> getAllFriends(String userId){
        List <Friendship> friends = friendshipRepo.findByMatchStatusAndUserIdOrFriendId("ACCEPTED", userId);
        List <LinkedProfile> linkedProfiles = new ArrayList<>();
        for (Friendship friend : friends){
            LinkedProfile linkedProfile = getFriendshipInfo(friend.getFriendshipId(), userId);
            linkedProfiles.add(linkedProfile);
        }
        return linkedProfiles;
    }
}
