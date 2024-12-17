package com.vibez.engine.Service.Implementation;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.Marketplace;
import com.vibez.engine.Repository.MarketplaceRepo;
import com.vibez.engine.Service.MarketplaceService;

@Service
public class MarketplaceImplement implements MarketplaceService{

    @Autowired
    private MarketplaceRepo marketplaceRepo;
    
    public boolean addtem(Marketplace newProduct){
        marketplaceRepo.save(newProduct);
        return true;
    }

    public Marketplace getItemById(ObjectId productId){
        Marketplace sellItem = marketplaceRepo.findByProductId(productId);
        return sellItem;
    }

    public List<Marketplace> getCommunityAndFriendsVisibleItems(List<ObjectId> frienIds){
        List<Marketplace> communityProducts = marketplaceRepo.findByVisibleToCommunityTrue();
        List<Marketplace> friendOnlyProducts = marketplaceRepo.findByVisibleToCommunityFalseAndSellerIdIn(frienIds);
        communityProducts.addAll(friendOnlyProducts);
        return communityProducts;
    }

    public String generateShareableLink(ObjectId productId){
        return "https://vibez.com/marketplace/item/" + productId.toHexString();
    }
}
