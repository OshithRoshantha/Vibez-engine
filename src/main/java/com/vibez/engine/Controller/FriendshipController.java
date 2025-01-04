package com.vibez.engine.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibez.engine.Model.Friendship;
import com.vibez.engine.Model.LinkedProfile;
import com.vibez.engine.Service.FriendshipService;

@CrossOrigin(origins = "*" , allowedHeaders = "*")
@RestController
@RequestMapping("/vibez")
public class FriendshipController {
    
    @Autowired
    private FriendshipService friendshipService;

    @GetMapping("/friends/{userId}") //find all friends of a user (not used in the frontend)
    public ResponseEntity<List<String>> getFriends(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId) {
        List<String> friends = friendshipService.getFriends(userId);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/friends/pending/{userId}") //find all pending requests of a user (not used in the frontend)
    public ResponseEntity<List<String>> getPendingRequests(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String userId) {
        List<String> pendingRequests = friendshipService.getPendingRequests(userId);
        return ResponseEntity.ok(pendingRequests);
    }
    
    @GetMapping("/friends/friendshipInfo/{friendshipId}/{userId}") //get friendship info
    public ResponseEntity<LinkedProfile> getFriendshipInfo(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String friendshipId, @PathVariable String userId){
        return ResponseEntity.ok(friendshipService.getFriendshipInfo(friendshipId, userId));
    }

    @GetMapping("/friends/{userId}/{friendId}") //get friendshipId
    public ResponseEntity<String> getFriendshipId(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String userId, @PathVariable String friendId){
        return ResponseEntity.ok(friendshipService.getFriendshipId(userId, friendId));
    }

    @GetMapping("/friends/check/{userId}/{friendshipId}") //check friendship
    public ResponseEntity<Boolean> checkFriendship(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String userId, @PathVariable String friendshipId){
        return ResponseEntity.ok(friendshipService.checkFriendship(userId, friendshipId));

    }

    @GetMapping("/friends/linked/{userId}") //get all linked profiles
    public ResponseEntity<List<String>> getLinkedProfiles(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String userId){
        return ResponseEntity.ok(friendshipService.getLinkedProfiles(userId));
    }
}