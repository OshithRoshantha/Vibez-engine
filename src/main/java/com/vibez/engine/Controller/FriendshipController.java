package com.vibez.engine.Controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibez.engine.Model.Friendship;
import com.vibez.engine.Service.FriendshipService;

@RestController
public class FriendshipController {
    
    @Autowired
    private FriendshipService friendshipService;

    @PostMapping("/friends/send/{userId}/{friendId}")
    public ResponseEntity<String> sendFriendRequest(@PathVariable ObjectId userId, @PathVariable ObjectId friendId) {
        String friendshipStatus = friendshipService.sendFriendRequest(userId, friendId);
        return ResponseEntity.ok(friendshipStatus);
    }

    @PutMapping ("/friends/accept/{userId}/{friendId}")
    public ResponseEntity<String> acceptFriendRequest(@PathVariable ObjectId userId, @PathVariable ObjectId friendId) {
        String friendshipStatus = friendshipService.acceptFriendRequest(userId, friendId);
        return ResponseEntity.ok(friendshipStatus);
    }

    @PutMapping("/friends/reject/{userId}/{friendId}")
    public ResponseEntity<String> rejectFriendRequest(@PathVariable ObjectId userId, @PathVariable ObjectId friendId) {
        String friendshipStatus = friendshipService.rejectFriendRequest(userId, friendId);
        return ResponseEntity.ok(friendshipStatus);
    }

    @PutMapping("/friends/block/{userId}/{friendId}")
    public ResponseEntity<String> blockFriend(@PathVariable ObjectId userId, @PathVariable ObjectId friendId) {
        String friendshipStatus = friendshipService.blockFriend(userId, friendId);
        return ResponseEntity.ok(friendshipStatus);
    }

    @GetMapping("/friends/{userId}")
    public ResponseEntity<List<Friendship>> getFriends(@PathVariable ObjectId userId) {
        List<Friendship> friends = friendshipService.getFriends(userId);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/friends/pending/{userId}")
    public ResponseEntity<List<Friendship>> getPendingRequests(@PathVariable ObjectId userId) {
        List<Friendship> pendingRequests = friendshipService.getPendingRequests(userId);
        return ResponseEntity.ok(pendingRequests);
    }    
}
