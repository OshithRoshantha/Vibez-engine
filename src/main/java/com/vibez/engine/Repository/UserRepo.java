package com.vibez.engine.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.vibez.engine.Model.User;

public interface UserRepo extends MongoRepository<User, String>{
    User findByEmail(String email);
    User findByUserId(String userId);
    @Query("{ $or: [ { 'email': { $regex: '^?0', $options: 'i' } }, { 'userName': { $regex: '^?0', $options: 'i' } } ] }")
    List<User> findByEmailOrUserNameStartingWith(String keyword);
}
