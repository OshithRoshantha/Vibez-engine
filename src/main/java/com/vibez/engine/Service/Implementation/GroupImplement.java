package com.vibez.engine.Service.Implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.Groups;
import com.vibez.engine.Repository.GroupRepo;
import com.vibez.engine.Service.GroupsService;

@Service
public class GroupImplement implements GroupsService{

    @Autowired
    private GroupRepo groupRepo;

    public String createGroup(Groups newGroup , String creatorId){
        newGroup.setCreatorId(creatorId);

        if (newGroup.getMemberIds() == null){
            newGroup.setMemberIds(new ArrayList<>());
        }

        newGroup.getMemberIds().add(creatorId);
        newGroup.setCreationDate(LocalDateTime.now()); 
        groupRepo.save(newGroup);
        return newGroup.getGroupId();
    }

    public boolean addUserToGroup(String groupId, String newUser){
        Groups group = groupRepo.findByGroupId(groupId);

        if (group.getMemberIds() == null){
            group.setMemberIds(new ArrayList<>());
        }

        group.getMemberIds().add(newUser); 
        groupRepo.save(group);
        return true;
    }

    public boolean removeUserFromGroup(String groupId, String existingUser){
        Groups group = groupRepo.findByGroupId(groupId);
        group.getMemberIds().remove(existingUser);
        groupRepo.save(group);
        return true;
    }

    public List<String> getGroupsByUser(String userId){
        List <Groups> groups = groupRepo.findByMemberIds(userId);
        List<String> groupIds = new ArrayList<>();
        for(Groups group : groups){
            groupIds.add(group.getGroupId());
        }
        return groupIds;
    }

    public boolean changeGroupIcon(String groupId, String newIcon){
        Groups group = groupRepo.findByGroupId(groupId);
        group.setGroupIcon(newIcon);
        groupRepo.save(group);
        return true;
    }

    public boolean changeGroupDescp(String groupId, String newDescp){
        Groups group = groupRepo.findByGroupId(groupId);
        group.setGroupDesc(newDescp);
        groupRepo.save(group);
        return true;
    }

    public boolean changeGroupName(String groupId, String newName){
        Groups group = groupRepo.findByGroupId(groupId);
        group.setGroupName(newName);
        groupRepo.save(group);
        return true;
    }

    public boolean sendGroupMessage(String groupId, String messageId){
        Groups group = groupRepo.findByGroupId(groupId);
        group.getMessageIds().add(messageId);
        groupRepo.save(group);
        return true;
    }

    public Groups getGroupById(String groupId){
        return groupRepo.findByGroupId(groupId);
    }
}
