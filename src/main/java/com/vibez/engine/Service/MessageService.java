package com.vibez.engine.Service;

import java.util.List;

import com.vibez.engine.Model.Message;

public interface MessageService {
    Message saveMessage(Message message);
    List<String> getDirectMessages(String senderId, String receiverId);
    List<String> getGroupMessages(String groupId);
    boolean markAsRead(String messageId);
}
