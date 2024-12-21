package com.vibez.engine.Service.Implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.Groups;
import com.vibez.engine.Model.Message;
import com.vibez.engine.Repository.GroupRepo;
import com.vibez.engine.Repository.MessageRepo;
import com.vibez.engine.Service.GroupsService;
import com.vibez.engine.Service.MessageService;

@Service
public class MessageImplement implements MessageService {

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private  GroupsService groupsService;

    @Autowired
    private  GroupRepo groupRepo;

    public Message sendMessage(Message message){
        message.setRead(false);
        message.setTimestamp(LocalDateTime.now());
        message = messageRepo.save(message);

        if (message.getGroupId() != null){
            Groups group = groupsService.getGroupById(message.getGroupId());
            if (group.getMessageIds() == null){
                group.setMessageIds(new ArrayList<>());
            }
              group.getMessageIds().add(message.getMessageId());
              groupRepo.save(group);
        }

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
