package com.vibez.engine.Model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "marketplace")
public class Marketplace {
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

    public ObjectId getSellerId() {
        return sellerId;
    }

    public void setSellerId(ObjectId sellerId) {
        this.sellerId = sellerId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getProductPhotos() {
        return productPhotos;
    }

    public void setProductPhotos(List<String> productPhotos) {
        this.productPhotos = productPhotos;
    }

    public boolean isVisibleToFriends() {
        return visibleToFriends;
    }

    public void setVisibleToFriends(boolean visibleToFriends) {
        this.visibleToFriends = visibleToFriends;
    }
}