package com.vibez.engine.Model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;

public class Groupsv_2 {
    @Id
    private String groupId;
    private String groupIcon;
    private String groupName;
    private String groupDesc;
    private String creatorId;
    private List<String> memberIds;
    private LocalDateTime creationDate;
    private  LocalDateTime lastUpdate;
    private  String lastMessage;
    private List<String> messageIds;

    public String getGroupId() {
        return groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getGroupIcon() {
        return groupIcon;
    }
    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }
    public String getGroupName() {
        return groupName;
    }
    
}
