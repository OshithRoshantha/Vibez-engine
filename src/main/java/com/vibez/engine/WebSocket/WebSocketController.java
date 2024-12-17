package com.vibez.engine.WebSocket;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller; 
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.vibez.engine.Model.Friendship;
import com.vibez.engine.Model.Message;
import com.vibez.engine.Service.FriendshipService;
import com.vibez.engine.Service.GroupsService;
import com.vibez.engine.Service.MessageService;

@Controller
public class WebSocketController extends TextWebSocketHandler {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private GroupsService groupService;

    @Autowired
    private FriendshipService friendshipService;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public Message sendMessage(Message message) {
        Message savedMessage = messageService.saveMessage(message);
        return savedMessage;
    }

    @MessageMapping("/addUserToGroup")
    public void addUserToGroup(ObjectId groupId, ObjectId newUser) {
        groupService.addUserToGroup(groupId, newUser);
        messagingTemplate.convertAndSend("/topic/group/" + groupId, newUser);
    }

    @MessageMapping("/changeGroupIcon")
    public void changeGroupIcon(ObjectId groupId, String newIcon) {
        groupService.changeGroupIcon(groupId, newIcon);
        messagingTemplate.convertAndSend("/topic/group/" + groupId, newIcon);
    }

    @MessageMapping("/changeGroupName")
    public void changeGroupName(ObjectId groupId, String newName) {
        groupService.changeGroupName(groupId, newName);
        messagingTemplate.convertAndSend("/topic/group/" + groupId, newName);
    }

    @MessageMapping("/sendFriendRequest")
    public void sendFriendRequest(ObjectId userId, ObjectId friendId) {
        String response = friendshipService.sendFriendRequest(userId, friendId);
        Friendship friendship = friendshipService.getPendingRequests(userId).stream()
                .filter(f -> f.getFriendId().equals(friendId))
                .findFirst()
                .orElse(null);
        if (friendship != null) {
            messagingTemplate.convertAndSend("/topic/friendship/" + userId, friendship);
            messagingTemplate.convertAndSend("/topic/friendship/" + friendId, friendship);
        }
    }

    @MessageMapping("/acceptFriendRequest")
    public void acceptFriendRequest(ObjectId userId, ObjectId friendId) {
        String response = friendshipService.acceptFriendRequest(userId, friendId);
        Friendship friendship = friendshipService.getFriends(userId).stream()
                .filter(f -> f.getFriendId().equals(friendId))
                .findFirst()
                .orElse(null);
        if (friendship != null) {
            messagingTemplate.convertAndSend("/topic/friendship/" + userId, friendship);
            messagingTemplate.convertAndSend("/topic/friendship/" + friendId, friendship);
        }
    }
}