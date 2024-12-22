package com.vibez.engine.Service;

import java.util.List;

import com.vibez.engine.Model.Groups;

public interface GroupsService {
    String createGroup(Groups newGroup, String creatorId);
    String addUsersToGroup(String groupId, List<String> newUsers);
    String removeUsersFromGroup(String groupId, List<String> existingUsers);
    List<String> getGroupsByUser(String user);
    Groups getGroupById(String groupId);
    String changeGroup(Groups updatedGroup);
}
