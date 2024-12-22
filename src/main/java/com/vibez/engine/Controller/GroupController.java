package com.vibez.engine.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    
    @PostMapping("group/create/{creatorId}")
    public ResponseEntity<String> createGroup(@RequestHeader(value = "Authorization", required = true) @RequestBody Groups newGroup, @PathVariable String creatorId){
        return ResponseEntity.ok(groupsService.createGroup(newGroup, creatorId));    
    }

    @PostMapping("group/{groupId}/{userId}")
    public ResponseEntity<Boolean> addUserToGroup(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String groupId, @PathVariable String userId){
        return ResponseEntity.ok(groupsService.addUserToGroup(groupId, userId));
    }

    @DeleteMapping("group/{groupId}/{memberId}")
    public ResponseEntity<Boolean> removeUserFromGroup(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String groupId, @PathVariable String memberId){
        return ResponseEntity.ok(groupsService.removeUserFromGroup(groupId, memberId));
    }

    @GetMapping("group/{userId}") //find all groups that a user is in
    public ResponseEntity<List<String>> getGroupsByUser(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId){
        return ResponseEntity.ok(groupsService.getGroupsByUser(userId));
    }

    @GetMapping("group/info/{groupId}") //get group info
    public ResponseEntity<Groups> getGroupInfo(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String groupId){
        return ResponseEntity.ok(groupsService.getGroupById(groupId));
    }

    @PutMapping("group/update")
    public ResponseEntity<String> changeGroup(@RequestHeader(value = "Authorization", required = true) @RequestBody Groups updateGroup){
        return ResponseEntity.ok(groupsService.changeGroup(updateGroup));
    }
}