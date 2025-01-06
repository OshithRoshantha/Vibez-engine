package com.vibez.engine.Service;

import com.vibez.engine.Model.Marketplace;

public interface MarketplaceService {
    Marketplace addItem(Marketplace newProduct);
    Marketplace updateItem(Marketplace updatedProduct);
    String deleteItem(String productId);
    Marketplace getItemById(String productId);
    //List<Marketplace> getProductsExcludingHiddenByFriends(ObjectId userId);
    String generateShareableLink(String itemId);
}