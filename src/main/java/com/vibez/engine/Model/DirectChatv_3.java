package com.vibez.engine.Model;

import java.time.LocalDateTime;
import java.util.List;

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
    
}
