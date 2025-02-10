package com.vibez.engine.Model;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "directChats")
public class Chats 
    @Id
    private String chatId;
    private List<String> memberIds;
    private LocalDateTime lastUpdate;
    private String lastMessage;
    private String lastMessageSender;
    private List<String> messageIds;
    private List<String> favoritedBy;
public String getChatId() {
    return chatId;
}

public void setChatId(String chatId) {
    this.chatId = chatId;
}
public List<String> getMemberIds() {
    return memberIds;
}
public String getGroupId() {
    String groupId;
        return groupIdpId();
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

