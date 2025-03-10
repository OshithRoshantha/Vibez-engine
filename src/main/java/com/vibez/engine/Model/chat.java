package com.vibez.engine.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "directChats")
public class chat {
    @Id
    private String chatId;
}
