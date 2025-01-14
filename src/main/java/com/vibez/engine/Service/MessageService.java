package com.vibez.engine.Service;

import java.util.List;

import com.vibez.engine.Model.Message;
import com.vibez.engine.Model.MessageInfo;

public interface MessageService {
    String sendMessage(Message message);
    Message getMessage(String messageId);
    List<String> getMessagesByGroups(String groupId);
    List<MessageInfo> getMessagesByDirectChat(String userId, String reciverId);
    void markAsRead(String userId, String chatId);
    int getUnReadCount(String userId);
}
