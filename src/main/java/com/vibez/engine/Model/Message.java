package com.vibez.engine.Model;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "messages")
public class Message {
    @Id
    private ObjectId messageId;
    private ObjectId senderId;
    private ObjectId receiverId;
    private ObjectId groupId; //null for direct messages
    private String message;
    private LocalDateTime timestamp;
    private boolean isRead;
}
