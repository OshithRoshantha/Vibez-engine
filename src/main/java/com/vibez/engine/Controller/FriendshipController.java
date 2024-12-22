package com.vibez.engine.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibez.engine.Service.FriendshipService;

@RestController
@RequestMapping("/vibez")
public class FriendshipController {
    
    @Autowired
    private FriendshipService friendshipService;

    @GetMapping("/friends/{userId}") //find all friends of a user
    public ResponseEntity<List<String>> getFriends(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId) {
        List<String> friends = friendshipService.getFriends(userId);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/friends/pending/{userId}") //find all pending requests of a user
    public ResponseEntity<List<String>> getPendingRequests(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String userId) {
        List<String> pendingRequests = friendshipService.getPendingRequests(userId);
        return ResponseEntity.ok(pendingRequests);
    }    
}