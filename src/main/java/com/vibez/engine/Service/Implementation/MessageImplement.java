package com.vibez.engine.Service.Implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.DirectChat;
import com.vibez.engine.Model.Groups;
import com.vibez.engine.Model.Message;
import com.vibez.engine.Repository.DirectChatRepo;
import com.vibez.engine.Repository.GroupRepo;
import com.vibez.engine.Repository.MessageRepo;
import com.vibez.engine.Service.DirectChatService;
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

    @Autowired
    private  DirectChatService directChatService;

    @Autowired
    private DirectChatRepo directChatRepo;

    public String sendMessage(Message message){
        message.setRead(false);
        message.setTimestamp(LocalDateTime.now());
        message = messageRepo.save(message);
        String updatingId = null;

        if (message.getGroupId() != null){
            Groups group = groupsService.getGroupById(message.getGroupId());
            group.setLastUpdate(LocalDateTime.now());
            group.setLastMessage(message.getMessage());
            if (group.getMessageIds() == null){
                group.setMessageIds(new ArrayList<>());
            }
              group.getMessageIds().add(message.getMessageId());
              groupRepo.save(group);
              updatingId = group.getGroupId();
        }

        else if (message.getDirectChatId() != null){
            DirectChat directChat = directChatService.getDirectChatById(message.getDirectChatId());
            directChat.setLastUpdate(LocalDateTime.now());
            directChat.setLastMessage(message.getMessage());
            if (directChat.getMessageIds() == null){
                directChat.setMessageIds(new ArrayList<>());
            }
            directChat.getMessageIds().add(message.getMessageId());
            directChatRepo.save(directChat);
            updatingId = directChat.getChatId();
        }

        return updatingId;
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

    public List<String> getMessagesByGroups(String groupId){
        List <Message> messages = messageRepo.findByGroupId(groupId);
        List<String> messageIds = new ArrayList<>();
        for (Message message : messages){
            messageIds.add(message.getMessageId());
        }
        return messageIds;
    }

    public List<String> getMessagesByDirectChat(String directChatId){
        List <Message> messages = messageRepo.findByDirectChatId(directChatId);
        List<String> messageIds = new ArrayList<>();
        for (Message message : messages){
            messageIds.add(message.getMessageId());
        }
        return messageIds;
    }

    public Message getMessage(String messageId){
        return messageRepo.findByMessageId(messageId);
    }
}
