package com.vibez.engine.Controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibez.engine.Model.Friendship;
import com.vibez.engine.Service.FriendshipService;

@RestController
@RequestMapping("/vibez")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/friends/send/{userId}/{friendId}")
    public ResponseEntity<String> sendFriendRequest(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable ObjectId userId, @PathVariable ObjectId friendId) {
        String friendshipStatus = friendshipService.sendFriendRequest(userId, friendId);
        if ("Friend request sent".equals(friendshipStatus)) {
            Friendship friendship = friendshipService.getPendingRequests(userId).stream()
                    .filter(f -> f.getFriendId().equals(friendId))
                    .findFirst()
                    .orElse(null);
            if (friendship != null) {
                messagingTemplate.convertAndSend("/topic/friendship/" + userId, friendship);
                messagingTemplate.convertAndSend("/topic/friendship/" + friendId, friendship);
            }
        }
        return ResponseEntity.ok(friendshipStatus);
    }

    @PutMapping("/friends/accept/{userId}/{friendId}")
    public ResponseEntity<String> acceptFriendRequest(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable ObjectId userId, @PathVariable ObjectId friendId) {
        String friendshipStatus = friendshipService.acceptFriendRequest(userId, friendId);
        if ("Friend request accepted".equals(friendshipStatus)) {
            Friendship friendship = friendshipService.getFriends(userId).stream()
                    .filter(f -> f.getFriendId().equals(friendId))
                    .findFirst()
                    .orElse(null);
            if (friendship != null) {
                messagingTemplate.convertAndSend("/topic/friendship/" + userId, friendship);
                messagingTemplate.convertAndSend("/topic/friendship/" + friendId, friendship);
            }
        }
        return ResponseEntity.ok(friendshipStatus);
    }

    @PutMapping("/friends/reject/{userId}/{friendId}")
    public ResponseEntity<String> rejectFriendRequest(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable ObjectId userId, @PathVariable ObjectId friendId) {
        String friendshipStatus = friendshipService.rejectFriendRequest(userId, friendId);
        if ("Friend request rejected".equals(friendshipStatus)) {
            Friendship friendship = new Friendship();
            friendship.setUserId(userId);
            friendship.setFriendId(friendId);
            friendship.setStatus("REJECTED");
            messagingTemplate.convertAndSend("/topic/friendship/" + userId, friendship);
            messagingTemplate.convertAndSend("/topic/friendship/" + friendId, friendship);
        }
        return ResponseEntity.ok(friendshipStatus);
    }

    @PutMapping("/friends/block/{userId}/{friendId}")
    public ResponseEntity<String> blockFriend(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable ObjectId userId, @PathVariable ObjectId friendId) {
        String friendshipStatus = friendshipService.blockFriend(userId, friendId);
        if ("Friend blocked".equals(friendshipStatus)) {
            Friendship friendship = new Friendship();
            friendship.setUserId(userId);
            friendship.setFriendId(friendId);
            friendship.setStatus("BLOCKED");
            messagingTemplate.convertAndSend("/topic/friendship/" + userId, friendship);
            messagingTemplate.convertAndSend("/topic/friendship/" + friendId, friendship);
        }
        return ResponseEntity.ok(friendshipStatus);
    }

    @GetMapping("/friends/{userId}")
    public ResponseEntity<List<Friendship>> getFriends(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable ObjectId userId) {
        List<Friendship> friends = friendshipService.getFriends(userId);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/friends/pending/{userId}")
    public ResponseEntity<List<Friendship>> getPendingRequests(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable ObjectId userId) {
        List<Friendship> pendingRequests = friendshipService.getPendingRequests(userId);
        return ResponseEntity.ok(pendingRequests);
    }
}