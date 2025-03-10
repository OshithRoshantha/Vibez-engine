package com.vibez.engine.Model;

import java.util.List;

import org.bson.types.ObjectId;

public class Marketplacev_2 {
    @Id
    private ObjectId productId;
    private ObjectId sellerId;
    private String productTitle;
    private String productDesc; 
    private String condition;
    private double price;
    private String location;
    private List<String> productPhotos;
    private boolean visibleToFriends;

    
}
