package com.vibez.engine.Model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

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


    public ObjectId getProductId() {
        return productId;
    }
    public void setProductId(ObjectId productId) {
        this.productId = productId;
    }
    
}
