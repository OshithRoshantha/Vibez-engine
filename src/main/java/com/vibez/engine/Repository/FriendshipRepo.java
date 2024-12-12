package com.vibez.engine.Repository;

import com.vibez.engine.Model.Friendship;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FriendshipRepo extends MongoRepository<Friendship, ObjectId> {
    @Query("{'status': ?0, '$or': [{'userId': ?1}, {'friendId': ?1}]}")
    List<Friendship> findByMatchStatusAndUserIdOrFriendId(String matchStatus, ObjectId matchId);
    Friendship findByUserIdAndFriendId(ObjectId userId, ObjectId friendId);
    List<Friendship> findByUserId(ObjectId userId);
}
