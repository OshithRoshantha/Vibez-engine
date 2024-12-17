package com.vibez.engine.WebSocket;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vibez.engine.Service.FriendshipService;
import com.vibez.engine.Service.GroupsService;
import com.vibez.engine.Service.MessageService;

public class VibezHandler implements WebSocketHandler {

    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private static final Map<String, List<WebSocketSession>> subscriptions = new ConcurrentHashMap<>();

    @Autowired
    private MessageService messageService;

    @Autowired
    private GroupsService groupsService;

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userEmail = (String) session.getAttributes().get("userEmail");
        sessions.put(userEmail, session);
        System.out.println("Connection established for user: " + userEmail);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = (String) message.getPayload();
        Map<String, Object> messageData = objectMapper.readValue(payload, Map.class);
        String action = (String) messageData.get("action");

        switch (action) {
            case "sendDirectMessage":
                handleSendDirectMessage(messageData);
                break;
            case "sendGroupMessage":
                handleSendGroupMessage(messageData);
                break;
            case "addUserToGroup":
                handleAddUserToGroup(messageData);
                break;
            case "removeUserFromGroup":
                handleRemoveUserFromGroup(messageData);
                break;
            case "changeGroupIcon":
                handleChangeGroupIcon(messageData);
                break;
            case "changeGroupName":
                handleChangeGroupName(messageData);
                break;
            case "changeGroupDesc":
                handleChangeGroupDesc(messageData);
                break;
            case "sendFriendRequest":
                handleSendFriendRequest(messageData);
                break;
            case "acceptFriendRequest":
                handleAcceptFriendRequest(messageData);
                break;
            case "rejectFriendRequest":
                handleRejectFriendRequest(messageData);
                break;
            case "blockFriend":
                handleBlockFriend(messageData);
                break;
            case "subscribe":
                handleSubscribe(session, messageData);
                break;
            default:
                System.out.println("Unknown action: " + action);
        }
    }

    private void handleSubscribe(WebSocketSession session, Map<String, Object> messageData) {
        String topic = (String) messageData.get("topic");
        subscriptions.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>()).add(session);
        System.out.println("User subscribed to topic: " + topic);
    }

    private void broadcastToSubscribers(String topic, Object message) {
        List<WebSocketSession> subscribers = subscriptions.get(topic);
        System.out.println("Broadcasting to topic: " + topic + " with message: " + message);
        if (subscribers != null) {
            System.out.println("Number of subscribers: " + subscribers.size());
            for (WebSocketSession subscriber : subscribers) {
                try {
                    subscriber.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
                    System.out.println("Message sent to subscriber: " + subscriber.getId());
                } catch (Exception e) {
                    System.out.println("Error delivering message to subscriber: " + e.getMessage());
                }
            }
        } else {
            System.out.println("No subscribers found for topic: " + topic);
        }
    }

    private void handleSendDirectMessage(Map<String, Object> messageData) {
        broadcastToSubscribers("sendDirectMessage", messageData);
    }

    private void handleSendGroupMessage(Map<String, Object> messageData) {
        // Implement the logic to handle sending group messages
    }

    private void handleAddUserToGroup(Map<String, Object> messageData) {
        // Implement the logic to handle adding a user to a group
    }

    private void handleRemoveUserFromGroup(Map<String, Object> messageData) {
        // Implement the logic to handle removing a user from a group
    }

    private void handleChangeGroupIcon(Map<String, Object> messageData) {
        // Implement the logic to handle changing the group icon
    }

    private void handleChangeGroupName(Map<String, Object> messageData) {
        // Implement the logic to handle changing the group name
    }

    private void handleChangeGroupDesc(Map<String, Object> messageData) {
        // Implement the logic to handle changing the group description
    }

    private void handleSendFriendRequest(Map<String, Object> messageData) {
        // Implement the logic to handle sending a friend request
    }

    private void handleAcceptFriendRequest(Map<String, Object> messageData) {
        // Implement the logic to handle accepting a friend request
    }

    private void handleRejectFriendRequest(Map<String, Object> messageData) {
        // Implement the logic to handle rejecting a friend request
    }

    private void handleBlockFriend(Map<String, Object> messageData) {
        // Implement the logic to handle blocking a friend
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("Error: " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String userEmail = (String) session.getAttributes().get("userEmail");
        sessions.remove(userEmail);
        subscriptions.values().forEach(list -> list.remove(session));
        System.out.println("Connection closed for user: " + userEmail);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}