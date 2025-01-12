package com.vibez.engine.Service.Implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.DirectChat;
import com.vibez.engine.Model.DirectChatPreview;
import com.vibez.engine.Model.User;
import com.vibez.engine.Repository.DirectChatRepo;
import com.vibez.engine.Repository.UserRepo;
import com.vibez.engine.Service.DirectChatService;
import com.vibez.engine.Service.UserService;

@Service
public class DirectChatImplemet implements DirectChatService{
    
    @Autowired
    private DirectChatRepo directChatRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

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
        User user1 = userService.getUserById(userId1);
        User user2 = userService.getUserById(userId2);
        memberIds.add(userId1);
        memberIds.add(userId2);
        newDirectChat.setMemberIds(memberIds);
        DirectChat savedDirectChat = directChatRepo.save(newDirectChat);
        if (user1.getDirectChatIds() == null){
            List<String> directChatIds = new ArrayList<>();
            directChatIds.add(savedDirectChat.getChatId());
            user1.setDirectChatIds(directChatIds);
            userRepo.save(user1);
        }
        else{
            user1.getDirectChatIds().add(savedDirectChat.getChatId());
            userRepo.save(user1);
        }
        if (user2.getDirectChatIds() == null){
            List<String> directChatIds = new ArrayList<>();
            directChatIds.add(savedDirectChat.getChatId());
            user2.setDirectChatIds(directChatIds);
            userRepo.save(user2);
        }
        else{
            user2.getDirectChatIds().add(savedDirectChat.getChatId());
            userRepo.save(user2);
        }
        return savedDirectChat.getChatId();
    }

    public boolean isAvailableDirectChat(String userId1, String userId2) {
        DirectChat chat = directChatRepo.findByBothMemberIds(userId1, userId2);
        if(chat == null){
            return false;
        }
        else {
            return true;
        }
    }

    public DirectChat getDirectChatById(String chatId) {
        return directChatRepo.findByChatId(chatId);
    }

    public String favoriteDirectChat(String chatId, String userId) {
        User user = userService.getUserById(userId);
        if(user.getFavoriteDirectChats() == null){
            List<String> favoriteDirectChats = new ArrayList<>();
            favoriteDirectChats.add(chatId);
            user.setFavoriteDirectChats(favoriteDirectChats);
            userRepo.save(user);
        }
        else{
            user.getFavoriteDirectChats().add(chatId);
            userRepo.save(user);
        }
        return "Chat favorited";
    }

    public String unfavoriteDirectChat(String chatId, String userId) {
        User user = userService.getUserById(userId);
        user.getFavoriteDirectChats().remove(chatId);
        userRepo.save(user);
        return "Chat unfavorited";
    }

    public DirectChatPreview getDirectChatPreview(String chatId, String userId) {
        DirectChat chat = directChatRepo.findByChatId(chatId);
        DirectChatPreview chatPreview = new DirectChatPreview();
        String friendId = chat.getMemberIds().stream()
                        .filter(memberId -> !memberId.equals(userId))
                        .findFirst()
                        .orElse(null);
        chatPreview.setFriendId(friendId);
        chatPreview.setFriendName(userService.getUserById(friendId).getUserName());
        chatPreview.setFriendAvatar(userService.getUserById(friendId).getProfilePicture());
        chatPreview.setLastMessage(chat.getLastMessage());
        chatPreview.setLastMessageSender(chat.getLastMessageSender());
        chatPreview.setLastActiveTime(chat.getLastUpdate());
        return chatPreview;
    }
    
}
