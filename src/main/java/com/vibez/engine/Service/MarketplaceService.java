package com.vibez.engine.Service;

import java.util.List;

import org.bson.types.ObjectId;

import com.vibez.engine.Model.Marketplace;

public interface MarketplaceService {
    Marketplace addItem(Marketplace newProduct);
    Marketplace updateItem(Marketplace updatedProduct);
    Marketplace getItemById(String productId);
    //List<Marketplace> getProductsExcludingHiddenByFriends(ObjectId userId);
    String generateShareableLink(String itemId);
}