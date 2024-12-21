package com.vibez.engine.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibez.engine.Service.FriendshipService;

@RestController
@RequestMapping("/vibez")
public class FriendshipController {
    
    @Autowired
    private FriendshipService friendshipService;

    @PostMapping("/friends/send/{userId}/{friendId}")
    public ResponseEntity<String> sendFriendRequest(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String userId, @PathVariable String friendId) {
        String friendshipStatus = friendshipService.sendFriendRequest(userId, friendId);
        return ResponseEntity.ok(friendshipStatus);
    }

    @PutMapping ("/friends/accept/{friendshipId}")
    public ResponseEntity<String> acceptFriendRequest(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String friendshipId) {
        String friendshipStatus = friendshipService.acceptFriendRequest(friendshipId);
        return ResponseEntity.ok(friendshipStatus);
    }

    @PutMapping("/friends/reject/{friendshipId}")
    public ResponseEntity<String> rejectFriendRequest(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String friendshipId) {
        String friendshipStatus = friendshipService.rejectFriendRequest(friendshipId);
        return ResponseEntity.ok(friendshipStatus);
    }

    @PutMapping("/friends/block/{friendshipId}")
    public ResponseEntity<String> blockFriend(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String friendshipId) {
        String friendshipStatus = friendshipService.blockFriend(friendshipId);
        return ResponseEntity.ok(friendshipStatus);
    }

     @GetMapping("/friends/{userId}")
    public ResponseEntity<List<String>> getFriends(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId) {
        List<String> friends = friendshipService.getFriends(userId);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/friends/pending/{userId}")
    public ResponseEntity<List<String>> getPendingRequests(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String userId) {
        List<String> pendingRequests = friendshipService.getPendingRequests(userId);
        return ResponseEntity.ok(pendingRequests);
    }    
}