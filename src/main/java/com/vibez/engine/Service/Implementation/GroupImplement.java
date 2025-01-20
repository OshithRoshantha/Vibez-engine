package com.vibez.engine.Service.Implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.Friendship;
import com.vibez.engine.Model.Groups;
import com.vibez.engine.Model.User;
import com.vibez.engine.Repository.FriendshipRepo;
import com.vibez.engine.Repository.GroupRepo;
import com.vibez.engine.Repository.UserRepo;
import com.vibez.engine.Service.FriendshipService;
import com.vibez.engine.Service.GroupsService;
import com.vibez.engine.Service.UserService;

@Service
public class GroupImplement implements GroupsService{

    @Autowired
    private GroupRepo groupRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private  FriendshipRepo friendshipRepo;

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
        newGroup.setLastMessageSender(newGroup.getGroupName());
        Groups savedGroup = groupRepo.save(newGroup);
        List <String> memberIds = savedGroup.getMemberIds();
        for(String memberId : memberIds){
            User member = userService.getUserById(memberId);
            if (member.getGroupIds() == null){
                member.setGroupIds(new ArrayList<>());
            }
            member.getGroupIds().add(newGroup.getGroupId());
            userRepo.save(member);
        }
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
        for (String user : newUsers){
            User member = userService.getUserById(user);
            if (member.getGroupIds() == null){
                member.setGroupIds(new ArrayList<>());
            }
            member.getGroupIds().add(groupId);
            userRepo.save(member);
        }
        groupRepo.save(group);
        return groupId;
    }

    public String removeUsersFromGroup(String groupId, List<String> existingUsers){
        Groups group = groupRepo.findByGroupId(groupId);
        for(String existingUser : existingUsers){
            group.getMemberIds().remove(existingUser);
        }
        for (String existingUser : existingUsers){
            User member = userService.getUserById(existingUser);
            member.getGroupIds().remove(groupId);
            userRepo.save(member);
        }
        groupRepo.save(group);
        return groupId;
    }

    public String deleteGroup(String groupId){
        Groups group = groupRepo.findByGroupId(groupId);
        List<String> memberIds = group.getMemberIds();
        for(String memberId : memberIds){
            User member = userService.getUserById(memberId);
            member.getGroupIds().remove(groupId);
            userRepo.save(member);
        }
        groupRepo.delete(group);
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

    public boolean isAdmin(String groupId, String userId){
        Groups group = groupRepo.findByGroupId(groupId);
        return group.getCreatorId().equals(userId);
    }

    public List<User> getAddList(String groupId, String userId) {
        Groups group = groupRepo.findByGroupId(groupId);
        List<Friendship> friendships = friendshipRepo.findByUserIdOrFriendId(userId);
        Set<String> friendIdsSet = new HashSet<>();
        for (Friendship friendship : friendships) {
            if (!friendship.getUserId().equals(userId)) {
                friendIdsSet.add(friendship.getUserId());
            }
            if (!friendship.getFriendId().equals(userId)) {
                friendIdsSet.add(friendship.getFriendId());
            }
        }
        List<String> friendIds = new ArrayList<>(friendIdsSet);
        List<User> addList = new ArrayList<>();
        List<String> memberIds = group.getMemberIds();
        for (String friendId : friendIds) {
            if (!memberIds.contains(friendId)) {
                User user = userRepo.findById(friendId).orElse(null); 
                if (user != null) {
                    addList.add(user);
                }
            }
        }
        return addList;
    }

    public boolean isRelated(String groupId, String userId) {
        if(groupId == null || userId == null) {
            return false;
        }
        Groups group = groupRepo.findByGroupId(groupId);
        if(group == null) {
            return false;
        }
        List<String> memberIds = group.getMemberIds();
        return memberIds.contains(userId);
    }

    public List<String> findGroup(String keyword, String userId) {
        List<Groups> groups = groupRepo.findByMemberIds(userId);
        List<String> groupIds = new ArrayList<>();
        String normalizedKeyword = keyword.toLowerCase();
        for (Groups group : groups) {
            if (group.getGroupName().toLowerCase().contains(normalizedKeyword)) {
                groupIds.add(group.getGroupId());
            }
        }
        return groupIds;
    }
    
}
