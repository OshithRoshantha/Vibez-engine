package com.vibez.engine.Service.Implementation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.Message;
import com.vibez.engine.Repository.MessageRepo;
import com.vibez.engine.Service.MessageService;

@Service
public class MessageImplement implements MessageService {

    @Autowired
    private MessageRepo messageRepo;

    public Message saveMessage(Message message){
        message.setRead(false);
        message.setTimestamp(LocalDateTime.now());
        message = messageRepo.save(message);
        return message;
    }

    public List<String> getDirectMessages(String senderId, String receiverId){
        return messageRepo.findBySenderIdAndReceiverId(senderId, receiverId);
    }

    public List<String> getGroupMessages(String groupId){
        return messageRepo.findByGroupId(groupId);
    }

    public boolean markAsRead(String messageId){
        Message message = messageRepo.findByMessageId(messageId);
        if(message == null){
            return false;
        }
        message.setRead(true);
        messageRepo.save(message);
        return true;
    }
}
