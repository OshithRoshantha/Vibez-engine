package com.vibez.engine.Service.Implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        List <Message> messages = messageRepo.findBySenderIdAndReceiverId(senderId, receiverId);
        List <String> messageIds = new ArrayList<>();
        for(Message message : messages){
            messageIds.add(message.getMessageId());
        }
        return messageIds;
    }

    public List<String> getGroupMessages(String groupId){
        List <Message> messages = messageRepo.findByGroupId(groupId);
        List <String> messageIds = new ArrayList<>();
        for(Message message : messages){
            messageIds.add(message.getMessageId());
        }
        return messageIds;
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
