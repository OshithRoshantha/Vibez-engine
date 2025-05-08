package com.vibez.engine.Model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "marketplace")
public class MarketplaceV2 {
    @Id
    private String productId;
    private String sellerId;
    private String sellerName;
    private String sellerProfilePicture;
    private String productTitle;
    private String productDesc;
    private String condition;
    private String price;
    private String location;
    private String listedDate;
    private List<String> productPhotos;
    private boolean visibleToFriends;
    private String productAction;
    private Integer totalClicks;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerProfilePicture() {
        return sellerProfilePicture;
    }

    public void setSellerProfilePicture(String sellerProfilePicture) {
        this.sellerProfilePicture = sellerProfilePicture;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getListedDate() {
        return listedDate;
    }

    public void setListedDate(String listedDate) {
        this.listedDate = listedDate;
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

    public String getProductAction() {
        return productAction;
   

