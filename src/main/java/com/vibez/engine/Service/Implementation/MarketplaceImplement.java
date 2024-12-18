package com.vibez.engine.Service.Implementation;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.Friendship;
import com.vibez.engine.Model.Marketplace;
import com.vibez.engine.Repository.MarketplaceRepo;
import com.vibez.engine.Service.FriendshipService;
import com.vibez.engine.Service.MarketplaceService;

@Service
public class MarketplaceImplement implements MarketplaceService {

    @Autowired
    private MarketplaceRepo marketplaceRepo;

    @Autowired
    private FriendshipService friendshipService;

    public boolean addItem(ObjectId sellerId, Marketplace newProduct) {
        newProduct.setSellerId(sellerId);
        marketplaceRepo.save(newProduct);
        return true;
    }

    public Marketplace getItemById(ObjectId productId) {
        return marketplaceRepo.findByProductId(productId);
    }

    public List<Marketplace> getProductsExcludingHiddenByFriends(ObjectId userId) {
        List<Friendship> friends = friendshipService.getFriends(userId);
        List<ObjectId> friendIds = new ArrayList<>();
        for (Friendship friend : friends) {
            friendIds.add(friend.getFriendId());
        }
        return marketplaceRepo.findAllSellingProducts(friendIds);
    }

    public String generateShareableLink(ObjectId productId) {
        return "http://localhost:8080/vibez/product/find/" + productId.toHexString();
    }
}