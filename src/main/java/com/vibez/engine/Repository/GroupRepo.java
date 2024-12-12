package com.vibez.engine.Repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.vibez.engine.Model.Groups;

public interface GroupRepo extends  MongoRepository<Groups, ObjectId>{
    List<Groups> findByMemberIdContaining(ObjectId memberId);
    Groups findByGroupId(ObjectId groupId);
}
