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
    
}
