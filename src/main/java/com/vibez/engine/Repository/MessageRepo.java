package com.vibez.engine.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vibez.engine.Model.Message;

public interface  MessageRepo extends MongoRepository<Message, String>{
    Message findByMessageId(String messageId);
    List <Message> findByGroupId(String groupId);
    List<Message> findByDirectChatId(String directChatId);
}
