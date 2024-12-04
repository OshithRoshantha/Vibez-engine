package com.vibez.engine.Model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "friendships")
public class Friendship {
    @Id
    private ObjectId friendshipId;
    private ObjectId userId;      
    private ObjectId friendId;    
    private FriendshipStatus status; 
    
    public ObjectId getFriendshipId() {
        return friendshipId;
    }

    public void setFriendshipId(ObjectId friendshipId) {
        this.friendshipId = friendshipId;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public ObjectId getFriendId() {
        return friendId;
    }

    public void setFriendId(ObjectId friendId) {
        this.friendId = friendId;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }
}

public enum FriendshipStatus {
    PENDING,      
    ACCEPTED,     
    BLOCKED,     
    REJECTED     
}
