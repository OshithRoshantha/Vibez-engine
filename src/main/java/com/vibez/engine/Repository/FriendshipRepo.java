package com.vibez.engine.Repository;

import com.vibez.engine.Model.Friendship;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FriendshipRepo extends MongoRepository<Friendship, ObjectId> {
    List<Friendship> findByUserIdAndStatus(ObjectId userId, String status);
    Friendship findByUserIdAndFriendId(ObjectId userId, ObjectId friendId);
    List<Friendship> findByUserId(ObjectId userId);
}
