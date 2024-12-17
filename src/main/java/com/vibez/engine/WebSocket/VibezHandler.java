/*package com.vibez.engine.WebSocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.vibez.engine.Model.Friendship;
import com.vibez.engine.Model.Message;
import com.vibez.engine.Service.FriendshipService;
import com.vibez.engine.Service.GroupsService;
import com.vibez.engine.Service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.bson.types.ObjectId;

@Component
public class VibezHandler implements WebSocketHandler {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private FriendshipService friendshipService;
    
    @Autowired
    private GroupsService groupService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connection established");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println("Message received: " + message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("Error: " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("Connection closed");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    
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

    @MessageMapping("/changeGroupDesc")
    public void changeGroupDesc(ObjectId groupId, String newDesc) {
        groupService.changeGroupDescp(groupId, newDesc);
        messagingTemplate.convertAndSend("/topic/group/" + groupId, newDesc);
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

    @MessageMapping("/rejectFriendRequest")
    public void rejectFriendRequest(ObjectId userId, ObjectId friendId) {
        String response = friendshipService.rejectFriendRequest(userId, friendId);
        Friendship friendship = new Friendship();
        friendship.setUserId(userId);
        friendship.setFriendId(friendId);
        friendship.setStatus("REJECTED");
        messagingTemplate.convertAndSend("/topic/friendship/" + userId, friendship);
        messagingTemplate.convertAndSend("/topic/friendship/" + friendId, friendship);
    }

    @MessageMapping("/blockFriend")
    public void blockFriend(ObjectId userId, ObjectId friendId) {
        String response = friendshipService.blockFriend(userId, friendId);
        Friendship friendship = new Friendship();
        friendship.setUserId(userId);
        friendship.setFriendId(friendId);
        friendship.setStatus("BLOCKED");
        messagingTemplate.convertAndSend("/topic/friendship/" + userId, friendship);
        messagingTemplate.convertAndSend("/topic/friendship/" + friendId, friendship);
    }
}
*/