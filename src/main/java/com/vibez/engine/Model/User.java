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
    private List<ObjectId> friendIds; 
    private List<ObjectId> groupIds;

    private static final String DEFAULT_PROFILE_PICTURE = "https://media.istockphoto.com/id/1300845620/vector/user-icon-flat-isolated-on-white-background-user-symbol-vector-illustration.jpg?s=612x612&w=0&k=20&c=yBeyba0hUkh14_jgv1OKqIH0CCSWU_4ckRkAoy2p73o=";
    private static final String DEFAULT_AB = "Hey there! I am using Vibez.";

    
}
