package com.vibez.engine.Service;

import java.util.List;

import com.vibez.engine.Model.Groups;

public interface GroupsService {
    boolean createGroup(Groups newGroup, String creatorId);
    boolean addUserToGroup(String groupId, String newUser);
    boolean removeUserFromGroup(String groupId, String existingUser);
    List<String> getGroupsByUser(String user);
    Groups getGroupById(String groupId);
    boolean changeGroupIcon(String groupId, String newIcon);
    boolean changeGroupDescp(String groupId, String newDescp);
    boolean changeGroupName(String groupId, String newName);
    boolean sendGroupMessage(String groupId, String messageId);
}
