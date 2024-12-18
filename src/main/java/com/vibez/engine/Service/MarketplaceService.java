package com.vibez.engine.Service;

import java.util.List;

import org.bson.types.ObjectId;

import com.vibez.engine.Model.Marketplace;

public interface MarketplaceService {
    boolean addItem(ObjectId sellerId, Marketplace newItem);
    Marketplace getItemById(ObjectId productId);
    List<Marketplace> getProductsExcludingHiddenByFriends(ObjectId userId);
    String generateShareableLink(ObjectId itemId);
}