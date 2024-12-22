package com.vibez.engine.Service;

import java.util.List;

import com.vibez.engine.Model.Message;

public interface MessageService {
    String sendMessage(Message message);
    Message getMessage(String messageId);
    List<String> getMessagesByGroups(String groupId);
    boolean markAsRead(String messageId);
}
