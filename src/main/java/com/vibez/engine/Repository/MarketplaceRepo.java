package com.vibez.engine.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.vibez.engine.Model.Marketplace;

public interface MarketplaceRepo extends MongoRepository<Marketplace, String> {
  List<Marketplace> findBySellerId(String sellerId);
  Marketplace findByProductId(String productId);
  @Query("{ $or: [ { 'visibleToFriends': true }, { 'sellerId': { $nin: ?0 } } ] }")
  List<Marketplace> findAllSellingProducts(List<String> friendIds);
  
}