package com.vibez.engine.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.vibez.engine.Model.Message;

public interface  MessageRepo extends MongoRepository<Message, String>{
    @Query("{ $or: [ { 'senderId': ?0, 'receiverId': ?1 }, { 'senderId': ?1, 'receiverId': ?0 } ] }")
    List<Message> findBySenderIdAndReceiverId(String senderId, String receiverId);
    List<Message> findByGroupId(String groupId);
    Message findByMessageId(String messageId);
}
