package com.vibez.engine.Service;

import java.util.List;

import com.vibez.engine.Model.Groups;
import com.vibez.engine.Model.User;

public interface GroupsService {
    String createGroup(Groups newGroup, String creatorId);
    String addUsersToGroup(String groupId, List<String> newUsers);
    String removeUsersFromGroup(String groupId, List<String> existingUsers);
    String deleteGroup(String groupId);
    List<String> getGroupsByUser(String user);
    Groups getGroupById(String groupId);
    String changeGroup(Groups updatedGroup);
    boolean isAdmin(String groupId, String userId);
    List<User> getAddList(String groupId, String userId);
    boolean isRelated(String groupId, String userId);
    List<String> findGroup(String keyword, String userId);
}
