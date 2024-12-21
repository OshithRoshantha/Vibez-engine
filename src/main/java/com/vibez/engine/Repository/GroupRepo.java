package com.vibez.engine.Repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.vibez.engine.Model.Groups;

public interface GroupRepo extends  MongoRepository<Groups, String>{
    List<Groups> findByMemberId(String userId);
    Groups findByGroupId(String groupId);
}
