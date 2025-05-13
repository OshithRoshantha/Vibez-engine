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

import com.vibez.engine.Model.FriendshipStatus;
import com.vibez.engine.Model.LinkedProfile;
import com.vibez.engine.Service.FriendshipService;

@CrossOrigin(origins = "*")
@RestController
//@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/vibez")
public class FriendshipController {
    
    @Autowired
    private FriendshipService friendshipService;

    @GetMapping("/friends/friendshipInfo/{friendshipId}/{userId}")
    public ResponseEntity<LinkedProfile> getFriendshipInfo(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String friendshipId, @PathVariable String userId){
        return ResponseEntity.ok(friendshipService.getFriendshipInfo(friendshipId, userId));
    }

    @GetMapping("/friends/{userId}/{friendId}") 
    public ResponseEntity<String> getFriendshipId(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String userId, @PathVariable String friendId){
        return ResponseEntity.ok(friendshipService.getFriendshipId(userId, friendId));
    }

    @GetMapping("/friends/isFriends/{userId}/{friendId}")
    public ResponseEntity<Boolean> isFriends(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String userId, @PathVariable String friendId){
        return ResponseEntity.ok(friendshipService.isFriends(userId, friendId));
    }

    @GetMapping("/friends/check/{userId}/{friendshipId}") 
    public ResponseEntity<Boolean> checkFriendship(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String userId, @PathVariable String friendshipId){
        return ResponseEntity.ok(friendshipService.checkFriendship(userId, friendshipId));
    }

    @GetMapping("/friends/linked/{userId}") 
    public ResponseEntity<List<String>> getLinkedProfiles(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String userId){
        return ResponseEntity.ok(friendshipService.getLinkedProfiles(userId));
    }

    @GetMapping("/friends/filterPendings/{userId}/{friendshipId}") 
    public ResponseEntity<Boolean> filterPendings(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String userId, @PathVariable String friendshipId){
        return ResponseEntity.ok(friendshipService.filterPendings(userId, friendshipId));
    }

    @GetMapping("/friends/filterAccepteds/{userId}/{friendshipId}")  
    public ResponseEntity<Boolean> filterAccepteds(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String userId, @PathVariable String friendshipId){
        return ResponseEntity.ok(friendshipService.filterAccepteds(userId, friendshipId));
    }

    @GetMapping("/friends/getStatus/{friendshipId}") 
    public ResponseEntity<FriendshipStatus> getStatus(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String friendshipId){
        return ResponseEntity.ok(friendshipService.getFriendshipStatus(friendshipId));
    }

    @GetMapping("/friends/getAll/{userId}")
    public ResponseEntity<List<LinkedProfile>> getAllFriends(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String userId){
        return ResponseEntity.ok(friendshipService.getAllFriends(userId));
    }
}