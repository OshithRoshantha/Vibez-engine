package com.vibez.engine.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.vibez.engine.Model.User;

public interface UserRepo extends MongoRepository<User, ObjectId>{
    User findByEmail(String email);
    User findByUserId(ObjectId userId);
}
