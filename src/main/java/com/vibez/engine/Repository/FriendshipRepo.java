package com.vibez.engine.Repository;

import com.vibez.engine.Model.Friendship;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FriendshipRepo extends MongoRepository<Friendship, String> {
    @Query("{'status': ?0, '$or': [{'userId': ?1}, {'friendId': ?1}]}")
    List<Friendship> findByMatchStatusAndUserIdOrFriendId(String matchStatus, String matchId);
    Friendship findByUserIdAndFriendId(String userId, String friendId);
    List<Friendship> findByUserId(String userId);
}
