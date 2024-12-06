package com.vibez.engine.Repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.vibez.engine.Model.Message;

public interface  MessageRepo extends MongoRepository<Message, ObjectId>{
    List<Message> findBySenderIdAndReceiverId(senderId, receiverId);
}
