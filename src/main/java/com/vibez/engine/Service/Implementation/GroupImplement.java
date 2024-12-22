package com.vibez.engine.Service.Implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.Groups;
import com.vibez.engine.Model.User;
import com.vibez.engine.Repository.GroupRepo;
import com.vibez.engine.Service.GroupsService;
import com.vibez.engine.Service.UserService;

@Service
public class GroupImplement implements GroupsService{

    @Autowired
    private GroupRepo groupRepo;

    @Autowired
    private UserService userService;

    public String createGroup(Groups newGroup , String creatorId){
        User creator = userService.getUserById(creatorId);
        newGroup.setCreatorId(creatorId);

        if (newGroup.getMemberIds() == null){
            newGroup.setMemberIds(new ArrayList<>());
        }

        newGroup.getMemberIds().add(creatorId);
        newGroup.setCreationDate(LocalDateTime.now());
        newGroup.setLastUpdate(LocalDateTime.now());
        newGroup.setLastMessage(creator.getUserName() + " created this group."); 
        groupRepo.save(newGroup);
        return newGroup.getGroupId();
    }

    public String addUsersToGroup(String groupId, List<String> newUsers){
        Groups group = groupRepo.findByGroupId(groupId);

        if (group.getMemberIds() == null){
            group.setMemberIds(new ArrayList<>());
        }
        for(String user : newUsers){
            group.getMemberIds().add(user);
        }

        groupRepo.save(group);
        return groupId;
    }

    public String removeUsersFromGroup(String groupId, List<String> existingUsers){
        Groups group = groupRepo.findByGroupId(groupId);

        for(String existingUser : existingUsers){
            group.getMemberIds().remove(existingUser);
        }

        groupRepo.save(group);
        return groupId;
    }

    public List<String> getGroupsByUser(String userId){
        List <Groups> groups = groupRepo.findByMemberIds(userId);
        List<String> groupIds = new ArrayList<>();
        for(Groups group : groups){
            groupIds.add(group.getGroupId());
        }
        return groupIds;
    }
    
    public Groups getGroupById(String groupId){
        return groupRepo.findByGroupId(groupId);
    }

    public String changeGroup(Groups updatedGroup){
        Groups group = groupRepo.findByGroupId(updatedGroup.getGroupId());
        if (updatedGroup.getGroupIcon() != null){
            group.setGroupIcon(updatedGroup.getGroupIcon());
        }
        if (updatedGroup.getGroupName() != null){
            group.setGroupName(updatedGroup.getGroupName());
        }
        if (updatedGroup.getGroupDesc() != null){
            group.setGroupDesc(updatedGroup.getGroupDesc());
        }
        groupRepo.save(group);
        return updatedGroup.getGroupId();
    }
}
