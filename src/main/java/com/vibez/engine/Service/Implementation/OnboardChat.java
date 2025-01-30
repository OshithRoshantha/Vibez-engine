package com.vibez.engine.Service.Implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.DirectChat;
import com.vibez.engine.Model.Friendship;
import com.vibez.engine.Model.Message;
import com.vibez.engine.Model.User;
import com.vibez.engine.Repository.DirectChatRepo;
import com.vibez.engine.Repository.FriendshipRepo;
import com.vibez.engine.Repository.MessageRepo;
import com.vibez.engine.Repository.UserRepo;

@Service
public class OnboardChat {

    @Autowired
    private FriendshipRepo friendshipRepo;

    @Autowired
    private DirectChatRepo directChatRepo;

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private UserRepo userRepo;

    public void sendGreetings(String userId){
        Friendship friendship = new Friendship();
        friendship.setUserId(userId);
        friendship.setFriendId("679b99e09b263c53d878d30f");
        friendship.setStatus("ACCEPTED");
        friendshipRepo.save(friendship);

        greetingMessage(userId, "🎉 Hey there! Welcome to VIBEZ! 🎊 We're so happy to have you here! 💜");
        greetingMessage(userId, "Now that you've joined, let me give you a quick tour! 🚀");
        greetingMessage(userId, "🔎 You can connect with others by searching for their username or email. Send them a request, and once they accept, you're all set to chat! 💬✨");
        greetingMessage(userId, "💡 Want to create a group chat? You totally can! Bring your friends together and vibe out in your own space! 🔥👥");
        greetingMessage(userId, "🛍️ Oh, and did I mention the Marketplace? You can list items for sale—privately for friends or publicly for everyone to see! Buyers can message you directly, and you can search for cool deals too! 💰🛒");
        greetingMessage(userId, "🌙 Prefer a dark mode? Check out the settings and switch up the vibe! 😎🌌");
        greetingMessage(userId, "That's it for now! 🎯 Start exploring, make connections, and enjoy your time on VIBEZ! 🚀💜");     
    }

    public void greetingMessage(String userId, String messageText) {

        Message message = new Message();
        message.setSenderId("679b99e09b263c53d878d30f");
        message.setReceiverId(userId);
        message.setMessage(messageText);
        message.setTimestamp(java.time.LocalDateTime.now());
        message.setRead(false);
        message = messageRepo.save(message);

        DirectChat directChat = directChatRepo.findByBothMemberIds("679b99e09b263c53d878d30f", userId);
        if (directChat == null){
            DirectChat newDirectChat = new DirectChat();
            newDirectChat.setLastUpdate(LocalDateTime.now());
            newDirectChat.setLastMessage(messageText);
            newDirectChat.setLastMessageSender("VIBEZ");
            List <String> memberIds = new ArrayList<>();
            List <String> messageIds = new ArrayList<>();
            messageIds.add(message.getMessageId());
            memberIds.add("679b99e09b263c53d878d30f");
            memberIds.add(userId);
            newDirectChat.setMemberIds(memberIds);
            newDirectChat.setMessageIds(messageIds);
            directChatRepo.save(newDirectChat);
            User user = userRepo.findByUserId(userId);
            user.getDirectChatIds().add(newDirectChat.getChatId());
            userRepo.save(user);
            User vibe = userRepo.findByUserId("679b99e09b263c53d878d30f");
            vibe.getDirectChatIds().add(newDirectChat.getChatId());
            userRepo.save(vibe);
        }
        else{
            directChat.setLastUpdate(LocalDateTime.now());
            directChat.setLastMessage(messageText);
            directChat.setLastMessageSender("VIBEZ");
            directChat.getMessageIds().add(message.getMessageId());
            directChatRepo.save(directChat);
        }
    }
}
