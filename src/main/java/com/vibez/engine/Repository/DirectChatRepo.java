package com.vibez.engine.Repository;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.vibez.engine.Model.DirectChat;

public interface  DirectChatRepo extends MongoRepository<DirectChat, String> {
    DirectChat findByChatId(String chatId);
    List<DirectChat> findByMemberIds(String userId);
    @Query("{ 'memberIds': { $all: [?0, ?1] } }")
    DirectChat findByBothMemberIds(String userId1, String userId2);
    List<DirectChat> findByMemberIdsContaining(String userId);

}
