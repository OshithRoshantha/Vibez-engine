package com.vibez.engine.Controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibez.engine.Model.Groups;
import com.vibez.engine.Service.GroupsService;

@RestController
@RequestMapping("/vibez")
public class GroupController {

    @Autowired
    private GroupsService groupsService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("group/create/{creatorId}")
    public ResponseEntity<Boolean> createGroup(@RequestHeader(value = "Authorization", required = true) @RequestBody Groups newGroup, @PathVariable ObjectId creatorId) {
        boolean result = groupsService.createGroup(newGroup, creatorId);
        if (result) {
            messagingTemplate.convertAndSend("/topic/groups", newGroup);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("group/{groupId}/{addedUser}")
    public ResponseEntity<Boolean> addUserToGroup(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable ObjectId groupId, @PathVariable ObjectId addedUser) {
        boolean result = groupsService.addUserToGroup(groupId, addedUser);
        if (result) {
            messagingTemplate.convertAndSend("/topic/group/" + groupId, addedUser);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("group/{groupId}/{addedUser}")
    public ResponseEntity<Boolean> removeUserFromGroup(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable ObjectId groupId, @PathVariable ObjectId addedUser) {
        boolean result = groupsService.removeUserFromGroup(groupId, addedUser);
        if (result) {
            messagingTemplate.convertAndSend("/topic/group/" + groupId, addedUser);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("group/{userId}")
    public ResponseEntity<List<Groups>> getGroupsByUser(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable ObjectId userId) {
        return ResponseEntity.ok(groupsService.getGroupsByUser(userId));
    }

    @PutMapping("group/name/{groupId}/{newName}")
    public ResponseEntity<Boolean> changeGroupName(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable ObjectId groupId, @PathVariable String newName) {
        boolean result = groupsService.changeGroupName(groupId, newName);
        if (result) {
            messagingTemplate.convertAndSend("/topic/group/" + groupId, newName);
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("group/desc/{groupId}/{newDesc}")
    public ResponseEntity<Boolean> changeGroupDesc(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable ObjectId groupId, @PathVariable String newDesc) {
        boolean result = groupsService.changeGroupDescp(groupId, newDesc);
        if (result) {
            messagingTemplate.convertAndSend("/topic/group/" + groupId, newDesc);
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("group/icon/{groupId}/{newIcon}")
    public ResponseEntity<Boolean> changeGroupIcon(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable ObjectId groupId, @PathVariable String newIcon) {
        boolean result = groupsService.changeGroupIcon(groupId, newIcon);
        if (result) {
            messagingTemplate.convertAndSend("/topic/group/" + groupId, newIcon);
        }
        return ResponseEntity.ok(result);
    }
}