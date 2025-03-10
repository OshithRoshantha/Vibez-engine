package com.vibez.engine.Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;

public class DirectChatv_3 {
    public class DirectChatv_2 {
    @Id
    private String chatId;
    private List<String> memberIds;
    private LocalDateTime lastUpdate;
    private String lastMessage;
    private List<String> messageIds;

    // Getters and Setters
    public String getChatId() {
        return chatId;
    }
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
    public List<String> getMemberIds() {
        return memberIds;
    }
    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public String getLastMessage() {
        return lastMessage;
    }
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;

}
public List<String> getMessageIds() {
    return messageIds;
}
public void setMessageIds(List<String> messageIds) {
    this.messageIds = messageIds;
}
public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DirectChat that = (DirectChat) o;
        return Objects.equals(chatId, that.chatId) &&
               Objects.equals(memberIds, that.memberIds) &&
               Objects.equals(lastUpdate, that.lastUpdate) &&
               Objects.equals(lastMessage, that.lastMessage) &&
               Objects.equals(messageIds, that.messageIds);
    }
    public int hashCode() {
        return Objects.hash(chatId, memberIds, lastUpdate, lastMessage, messageIds);
    }
    public String toString() {
        return "DirectChat{" +
               "chatId='" + chatId + '\'' +
               ", memberIds=" + memberIds +
               ", lastUpdate=" + lastUpdate +
               ", lastMessage='" + lastMessage + '\'' +
               ", messageIds=" + messageIds +
               '}';
    }
}


    }
