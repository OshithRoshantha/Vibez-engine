package com.vibez.engine.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vibez.engine.Model.User;

public interface UserRepo extends MongoRepository<User, String>{
    User findByEmail(String email);
    User findByUserId(String userId);
}
