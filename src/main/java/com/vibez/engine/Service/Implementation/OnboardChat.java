package com.vibez.engine.Service.Implementation;

import com.vibez.engine.Model.Message;
import com.vibez.engine.Service.FriendshipService;
import com.vibez.engine.Service.MessageService;

public class OnboardChat {

    private FriendshipService friendshipService;
    private MessageService messageService;

    public void sendGreetings(String userId){
        String vibezShip = friendshipService.sendFriendRequest(userId, "679b99e09b263c53d878d30f");
        friendshipService.acceptFriendRequest(vibezShip);
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
        messageService.sendMessage(message);
    }
}
