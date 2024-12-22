package com.vibez.engine.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "friendships")
public class Friendship {
    @Id
    private String friendshipId;
    private String userId;      
    private String friendId;    
    private String status; 
    
    public String getFriendshipId() {
        return friendshipId;
    }
    public void setFriendshipId(String friendshipId) {
        this.friendshipId = friendshipId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getFriendId() {
        return friendId;
    }
    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

