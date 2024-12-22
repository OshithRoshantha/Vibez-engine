package com.vibez.engine.Service;

import java.util.List;

import com.vibez.engine.Model.Groups;

public interface GroupsService {
    String createGroup(Groups newGroup, String creatorId);
    boolean addUserToGroup(String groupId, String newUser);
    boolean removeUserFromGroup(String groupId, String existingUser);
    List<String> getGroupsByUser(String user);
    Groups getGroupById(String groupId);
    String changeGroup(Groups updatedGroup);
}
