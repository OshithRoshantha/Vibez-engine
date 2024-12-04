package com.vibez.engine.Model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    @Id
    private ObjectId userId;
    private String userName;
    private String email; 
    private String password; 
    private String profilePicture; 
    private String about; 
    private List<ObjectId> friendshipIds; 
    private List<ObjectId> groupIds;
    
    public String getUserName() {
        return userName;
    }   
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    public String getAbout() {
        return about;
    }
    public void setAbout(String about) {
        this.about = about;
    }
    public List<ObjectId> getFriendshipIds() {
        return friendshipIds;
    }
    public void setFriendshipIds(List<ObjectId> friendshipIds) {
        this.friendshipIds = friendshipIds;
    }
    public List<ObjectId> getGroupIds() {
        return groupIds;
    }
    public void setGroupIds(List<ObjectId> groupIds) {
        this.groupIds = groupIds;
    }
}
