package com.vibez.engine.Controller;

import java.util.List;

import org.bson.types.ObjectId;
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
    public ResponseEntity<Boolean> createGroup(@RequestHeader(value = "Authorization", required = true) @RequestBody Groups newGroup, @PathVariable ObjectId creatorId){
        return ResponseEntity.ok(groupsService.createGroup(newGroup, creatorId));    
    }

    @PostMapping("group/{groupId}/{addedUser}")
    public ResponseEntity<Boolean> addUserToGroup(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable ObjectId groupId, @PathVariable ObjectId addedUser){
        return ResponseEntity.ok(groupsService.addUserToGroup(groupId, addedUser));
    }

    @DeleteMapping("group/{groupId}/{addedUser}")
    public ResponseEntity<Boolean> removeUserFromGroup(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable ObjectId groupId, @PathVariable ObjectId addedUser){
        return ResponseEntity.ok(groupsService.removeUserFromGroup(groupId, addedUser));
    }

    @GetMapping("group/{userId}")
    public ResponseEntity<List<Groups>> getGroupsByUser(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable ObjectId userId){
        return ResponseEntity.ok(groupsService.getGroupsByUser(userId));
    }

    @PutMapping("group/name/{groupId}/{newName}")
    public ResponseEntity<Boolean> changeGroupName(@RequestHeader(value = "Authorization", required = true)  String token,  @PathVariable ObjectId groupId, @PathVariable String newName){
        return ResponseEntity.ok(groupsService.changeGroupName(groupId, newName));
    }

    @PutMapping("group/desc/{groupId}/{newDesc}")
    public ResponseEntity<Boolean> changeGroupDesc(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable ObjectId groupId, @PathVariable String newDesc){
        return ResponseEntity.ok(groupsService.changeGroupDescp(groupId, newDesc));
    }

    @PutMapping("group/icon/{groupId}/{newIcon}")
    public ResponseEntity<Boolean> changeGroupIcon(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable ObjectId groupId, @PathVariable String newIcon){
        return ResponseEntity.ok(groupsService.changeGroupIcon(groupId, newIcon));
    }
}
