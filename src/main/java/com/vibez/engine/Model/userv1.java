package com.vibez.engine.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class userv1 {  // Changed class name to follow naming conventions
    @Id
    private String userId;
    private String userName;  // Moved inside class
    private String email; 
    private String password; 
    private String profilePicture; 
    private String about;

    // Constructor (optional)
    public userv1() {}

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public String getAbout() { return about; }
    public void setAbout(String about) { this.about = about; }
}
