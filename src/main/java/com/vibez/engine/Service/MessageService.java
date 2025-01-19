package com.vibez.engine.Service;

import java.util.List;

import com.vibez.engine.Model.GroupMessageInfo;
import com.vibez.engine.Model.Message;
import com.vibez.engine.Model.MessageInfo;

public interface MessageService {
    String sendMessage(Message message);
    Message getMessage(String messageId);
    List<String> getMessagesByGroups(String groupId);
    List<MessageInfo> getMessagesByDirectChat(String userId, String reciverId);
    void markAsRead(String userId, String receiverId);
    void markAsReadGroup(String userId, String groupId);
    int getUnReadCount(String userId);
    Boolean checkUnreadMessages(String chatId, String userId);
    int unreadGroupMessageCount(String userId);
    List<String> getMessageHistory(String userId, String reciverId);
    List<GroupMessageInfo> getMessagesByGroupChat(String userId, String groupId);
}
