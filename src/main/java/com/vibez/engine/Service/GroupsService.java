package com.vibez.engine.Service;

import java.util.List;

import org.bson.types.ObjectId;

import com.vibez.engine.Model.Groups;

public interface GroupsService {
    boolean createGroup(Groups newGroup);
    boolean addUserToGroup(ObjectId groupId, ObjectId newUser);
    boolean removeUserFromGroup(ObjectId groupId, ObjectId existingUser);
    List<Groups> getGroupsByUser(ObjectId user);
    boolean changeGroupIcon(ObjectId groupId, String newIcon);
    boolean changeGroupDescp(ObjectId groupId, String newDescp);
    boolean changeGroupName(ObjectId groupId, String newName);
}
