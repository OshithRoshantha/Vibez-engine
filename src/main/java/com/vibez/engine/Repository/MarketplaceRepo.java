package com.vibez.engine.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vibez.engine.Model.Marketplace;
import org.bson.types.ObjectId;


public interface MarketplaceRepo extends MongoRepository <Marketplace, ObjectId> {
    List<Marketplace> findByVisibleToCommunityTrue();
    List<Marketplace> findBySellerId(ObjectId sellerId);
    List<Marketplace> findByVisibleToCommunityFalseAndSellerIdIn(List<ObjectId> friendId);
}
