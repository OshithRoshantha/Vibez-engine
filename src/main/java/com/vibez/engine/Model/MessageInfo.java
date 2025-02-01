package com.vibez.engine.Model;

public class MessageInfo {
    Boolean isSendByMe;
    String message;
    String timestamp;
    String sender;
    String senderName;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsSendByMe() {
        return isSendByMe;
    }

    public void setIsSendByMe(Boolean isSendByMe) {
        this.isSendByMe = isSendByMe;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
