package com.vibez.engine.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    
    @GetMapping("group/{userId}") //find all groups that a user is in
    public ResponseEntity<List<String>> getGroupsByUser(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId){
        return ResponseEntity.ok(groupsService.getGroupsByUser(userId));
    }

    @GetMapping("group/info/{groupId}") //get group info
    public ResponseEntity<Groups> getGroupInfo(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String groupId){
        return ResponseEntity.ok(groupsService.getGroupById(groupId));
    }
}