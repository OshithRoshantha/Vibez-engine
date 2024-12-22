package com.vibez.engine.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.vibez.engine.Model.Friendship;

public interface FriendshipRepo extends MongoRepository<Friendship, String> {
    @Query("{'status': ?0, '$or': [{'userId': ?1}, {'friendId': ?1}]}")
    List<Friendship> findByMatchStatusAndUserIdOrFriendId(String matchStatus, String matchId);
    Friendship findByUserIdAndFriendId(String userId, String friendId);
    Friendship findByFriendshipId(String friendshipId);
    List<Friendship> findByUserId(String userId);
}
