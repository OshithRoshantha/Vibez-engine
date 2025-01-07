package com.vibez.engine.Service;

import java.util.List;

import com.vibez.engine.Model.Marketplace;

public interface MarketplaceService {
    Marketplace addItem(Marketplace newProduct);
    Marketplace updateItem(Marketplace updatedProduct);
    String deleteItem(String productId);
    Marketplace getItemById(String productId);
    List<Marketplace> getProductsExcludingHiddenByFriends(String userId);
    String generateShareableLink(String itemId);
    Integer getActiveListingCount(String sellerId);
    List<Marketplace> getMyItems(String sellerId);
    boolean isSeller(String userId);
}