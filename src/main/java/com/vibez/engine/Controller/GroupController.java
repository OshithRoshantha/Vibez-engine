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

import com.vibez.engine.Model.Groups;
import com.vibez.engine.Model.User;
import com.vibez.engine.Service.GroupsService;

@CrossOrigin(origins = "*" , allowedHeaders = "*")
@RestController
@RequestMapping("/vibez")
public class GroupController {

    @Autowired
    private GroupsService groupsService;

    @GetMapping("/group/info/{groupId}") //get group info
    public ResponseEntity<Groups> getGroupInfo(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String groupId){
        return ResponseEntity.ok(groupsService.getGroupById(groupId));
    }

    @GetMapping("/group/isAdmin/{groupId}/{userId}") //check if user is admin
    public ResponseEntity<Boolean> isAdmin(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String groupId, @PathVariable String userId){
        return ResponseEntity.ok(groupsService.isAdmin(groupId, userId));
    }

    @GetMapping("/group/getAddList/{groupId}/{userId}") //get list of users that can be added to group
    public ResponseEntity<List<User>> getAddList(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String groupId, @PathVariable String userId){
        return ResponseEntity.ok(groupsService.getAddList(groupId, userId));
    }

    @GetMapping("/group/isRelated/{groupId}/{userId}") //check if user is related to group
    public ResponseEntity<Boolean> isRelated(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String groupId, @PathVariable String userId){
        return ResponseEntity.ok(groupsService.isRelated(groupId, userId));
    }
}