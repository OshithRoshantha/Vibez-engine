package com.vibez.engine.Service.Implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.DirectChat;
import com.vibez.engine.Repository.DirectChatRepo;
import com.vibez.engine.Service.DirectChatService;

@Service
public class DirectChatImplemet implements DirectChatService{
    
    @Autowired
    private DirectChatRepo directChatRepo;

    public List<String> getDirectChatsByUser(String userId) {
        List<DirectChat> directChat = directChatRepo.findByMemberIds(userId);
        List<String> chatIds = new ArrayList<>();
        for(DirectChat chat : directChat){
            chatIds.add(chat.getChatId());
        }
        return chatIds;
    }

    public String createDirectChat(String userId1, String userId2) {
        DirectChat newDirectChat = new DirectChat();
        List <String> memberIds = new ArrayList<>();
        memberIds.add(userId1);
        memberIds.add(userId2);
        newDirectChat.setMemberIds(memberIds);
        directChatRepo.save(newDirectChat);
        return newDirectChat.getChatId();
    }

    public DirectChat getDirectChatById(String chatId) {
        return directChatRepo.findByChatId(chatId);
    }
    
}
