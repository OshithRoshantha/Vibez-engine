package com.vibez.engine.Model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "users")
public class userv1 {
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
}
