package com.vibez.engine.Service;

import java.util.List;

import org.bson.types.ObjectId;

import com.vibez.engine.Model.Message;

public interface MessageService {
    String saveMessage(Message message);
    List<Message> getDirectMessages(ObjectId senderId, ObjectId receiverId);
    List<Message> getGroupMessages(ObjectId groupId);
    String markAsRead(ObjectId messageId);
}
