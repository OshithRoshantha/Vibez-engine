package com.vibez.engine.Service.Implementation;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.Message;
import com.vibez.engine.Repository.MessageRepo;
import com.vibez.engine.Service.MessageService;

@Service
public class MessageImplement implements MessageService {

    @Autowired
    private MessageRepo messageRepo;

    public boolean  saveMessage(Message message){
        message = messageRepo.save(message);
        return true;
    }

    public List<Message> getDirectMessages(ObjectId senderId, ObjectId receiverId){
        return messageRepo.findBySenderIdAndReceiverId(senderId, receiverId);
    }

    public List<Message> getGroupMessages(ObjectId groupId){
        return messageRepo.findByGroupId(groupId);
    }

    public boolean markAsRead(ObjectId messageId){
        Message message = messageRepo.findByMessageId(messageId);
        if(message == null){
            return false;
        }
        message.setRead(true);
        messageRepo.save(message);
        return true;
    }
}
