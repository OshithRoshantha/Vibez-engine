package com.vibez.engine.Service.Implementation;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.Groups;
import com.vibez.engine.Repository.GroupRepo;
import com.vibez.engine.Service.GroupsService;

@Service
public class GroupImplement implements GroupsService{

    @Autowired
    private GroupRepo groupRepo;

    public boolean createGroup(Groups newGroup , ObjectId creatorId){
        newGroup.setCreatorId(creatorId);
        newGroup.setMemberId(List.of(creatorId));
        newGroup.setCreationDate(LocalDateTime.now());  
        groupRepo.save(newGroup);
        return true;
    }

    public boolean addUserToGroup(ObjectId groupId, ObjectId newUser){
        Groups group = groupRepo.findByGroupId(groupId);
        group.getMemberId().add(newUser);
        groupRepo.save(group);
        return true;
    }

    public boolean removeUserFromGroup(ObjectId groupId, ObjectId existingUser){
        Groups group = groupRepo.findByGroupId(groupId);
        group.getMemberId().remove(existingUser);
        groupRepo.save(group);
        return true;
    }

    public List<Groups> getGroupsByUser(ObjectId user){
        return groupRepo.findByMemberIdContaining(user);
    }

    public boolean changeGroupIcon(ObjectId groupId, String newIcon){
        Groups group = groupRepo.findByGroupId(groupId);
        group.setGroupIcon(newIcon);
        groupRepo.save(group);
        return true;
    }

    public boolean changeGroupDescp(ObjectId groupId, String newDescp){
        Groups group = groupRepo.findByGroupId(groupId);
        group.setGroupDesc(newDescp);
        groupRepo.save(group);
        return true;
    }

    public boolean changeGroupName(ObjectId groupId, String newName){
        Groups group = groupRepo.findByGroupId(groupId);
        group.setGroupName(newName);
        groupRepo.save(group);
        return true;
    }
}
