package com.vibez.engine.Model;

import java.util.List;

public class RabbitMQ {
    private String action;
    private String id;
    private String sender;
    private String type;
    private List<String> relatedIds;
    private String chatId;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<String> getRelatedIds() {
        return relatedIds;
    }

    public void setRelatedIds(List<String> relatedIds) {
        this.relatedIds = relatedIds;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
