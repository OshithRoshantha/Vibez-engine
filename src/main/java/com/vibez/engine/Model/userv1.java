package com.vibez.engine.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class userv1 {
    @Id
    private String userId;
}
