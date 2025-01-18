package com.vibez.engine.Model;

public class GroupMessageInfo {
    Boolean isSendByMe;
    String message;
    String timestamp;
    String sender;

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
}
