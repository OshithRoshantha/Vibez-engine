package com.vibez.engine.Model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "groups")
public class Groups {
    @Id
    private ObjectId groupId;
    private String gropuIcon;
    private String groupName;
    private String groupDescription;
    private ObjectId creatorId;
    private List<ObjectId> memberIds;
}
