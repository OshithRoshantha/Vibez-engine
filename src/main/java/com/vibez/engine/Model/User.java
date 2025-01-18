package com.vibez.engine.Model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "users")
public class User {
    @Id
    private String userId;
    private String userName;
    private String email; 
    private String password; 
    private String profilePicture; 
    private String about; 
    private boolean darkMode;
    private String publicKey;
    private List<String> blockedUsers;
    private List<String> directChatIds;
    private List<String> groupIds;
    private List<String> favoriteDirectChats;

    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

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

    public boolean getDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public List<String> getBlockedUsers() {
        return blockedUsers;
    }

    public void setBlockedUsers(List<String> blockedUsers) {
        this.blockedUsers = blockedUsers;
    }

    public List<String> getDirectChatIds() {
        return directChatIds;
    }

    public void setDirectChatIds(List<String> directChatIds) {
        this.directChatIds = directChatIds;
    }

    public List<String> getFavoriteDirectChats() {
        return favoriteDirectChats;
    }

    public void setFavoriteDirectChats(List<String> favoriteDirectChats) {
        this.favoriteDirectChats = favoriteDirectChats;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<String> groupIds) {
        this.groupIds = groupIds;
    }

}
