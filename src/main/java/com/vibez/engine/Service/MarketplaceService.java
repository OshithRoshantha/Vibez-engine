package com.vibez.engine.Service;

import java.util.List;

import org.bson.types.ObjectId;

import com.vibez.engine.Model.Marketplace;

public interface MarketplaceService {
    boolean addtem(Marketplace newItem);
    Marketplace getItemById(ObjectId productId);
    List<Marketplace> getCommunityAndFriendsVisibleItems(List<ObjectId> frienIds);
    String generateShareableLink(ObjectId itemId);
}
