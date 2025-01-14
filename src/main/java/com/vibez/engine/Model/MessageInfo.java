package com.vibez.engine.Model;

public class MessageInfo {
    Boolean isSendByMe;
    String message;
    String timestamp;

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
}
