package com.vibez.engine.Service;

import java.util.List;

import com.vibez.engine.Model.Message;

public interface MessageService {
    Message sendMessage(Message message);
    List<String> getDirectMessages(String senderId, String receiverId);
    boolean markAsRead(String messageId);
}
