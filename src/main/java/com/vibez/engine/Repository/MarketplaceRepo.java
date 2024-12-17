package com.vibez.engine.Repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.vibez.engine.Model.Marketplace;

public interface MarketplaceRepo extends MongoRepository <Marketplace, ObjectId> {
    List<Marketplace> findBySellerId(ObjectId sellerId);
    Marketplace findByProductId(ObjectId productId);
    @Query("""
        {
          $or: [
            { "visibleToFriends": true },
            {
              "visibleToFriends": false,
              "sellerId": { $nin: ?0 }
            }
          ]
        }
        """)
    List<Marketplace> findProductsExcludingFriends(List<ObjectId> friendIds);
}
