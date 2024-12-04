package com.vibez.engine.Model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "friendships")
public class Friendship {
    @Id
    private ObjectId friendshipId;
    private ObjectId userId;      
    private ObjectId friendId;    
    private String status; 
    
    @JsonProperty("friendshipId")
    public String getFriendshipId() {
        return friendshipId != null ? friendshipId.toHexString() : null;
    }

    public void setFriendshipId(String friendshipId) {
        this.friendshipId = new ObjectId(friendshipId);
    }

    @JsonProperty("userId")
    public String getUserId() {
        return userId != null ? userId.toHexString() : null;
    }

    public void setUserId(String userId) {
        this.userId = new ObjectId(userId);
    }

    @JsonProperty("friendId")
    public String getFriendId() {
        return friendId != null ? friendId.toHexString() : null;
    }

    public void setFriendId(String friendId) {
        this.friendId = new ObjectId(friendId);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

