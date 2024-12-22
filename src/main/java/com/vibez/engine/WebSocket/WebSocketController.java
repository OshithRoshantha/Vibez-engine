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
import com.vibez.engine.Model.Friendship;
import com.vibez.engine.Model.GroupAction;
import com.vibez.engine.Model.Groups;
import com.vibez.engine.Model.Message;
import com.vibez.engine.Service.FriendshipService;
import com.vibez.engine.Service.GroupsService;
import com.vibez.engine.Service.MessageService;

public class WebSocketController implements WebSocketHandler {

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
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = (String) message.getPayload();
        Map<String, Object> messageData = objectMapper.readValue(payload, Map.class);
        String action = (String) messageData.get("action");

        switch (action) {
            case "messageService":
                handleMessages(messageData);
                break;
            case "friendshipService":
                handleFriendships(messageData);
                break;
            case "groupService":
                handleGroups(messageData);
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
    }

    private void broadcastToSubscribers(String topic, Object message) {
        List<WebSocketSession> subscribers = subscriptions.get(topic);
        if (subscribers != null) {
            for (WebSocketSession subscriber : subscribers) {
                try {
                    subscriber.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
                } catch (Exception e) {
                    System.out.println("Error delivering message to subscriber: " + e.getMessage());
                }
            }
        }
    }

    private void handleMessages(Map<String, Object> messageData) {
        Message message = objectMapper.convertValue(messageData.get("body"), Message.class);
        messageService.sendMessage(message);
        broadcastToSubscribers("messageService", messageData.get("body"));
    }

    private void handleFriendships(Map<String, Object> messageData) {
        Friendship friendship = objectMapper.convertValue(messageData.get("body"), Friendship.class);
        String friendshipId = null;
        if (friendship.getFriendshipId() == null) {
            friendshipId = friendshipService.sendFriendRequest(friendship.getUserId(), friendship.getFriendId());
        } else {
            friendshipId = friendship.getFriendshipId();
            if (friendship.getStatus().equals("ACCEPTED")) {
                friendshipService.acceptFriendRequest(friendship.getFriendshipId());
            } else if (friendship.getStatus().equals("REJECTED")) {
                friendshipService.unFriend(friendship.getFriendshipId());
            } else if (friendship.getStatus().equals("BLOCKED")) {
                friendshipService.blockFriend(friendship.getFriendshipId());
            }
        }
        broadcastToSubscribers("friendshipService", friendshipId);
    }

    private void handleGroups(Map<String, Object> messageData) {
        String groupId = null;
        if (messageData.get("groupAction") != null){
            GroupAction groupAction = objectMapper.convertValue(messageData.get("groupAction"), GroupAction.class);
            if (groupAction.getAction().equals("addUsers")){
                groupId = groupsService.addUsersToGroup(groupAction.getGroupId(), groupAction.getUserIds());
            } else if (groupAction.getAction().equals("removeUsers")){
                groupId = groupsService.removeUsersFromGroup(groupAction.getGroupId(), groupAction.getUserIds());
            }
            broadcastToSubscribers("groupService", groupId);
            return;
        }
        if (messageData.get("body") != null){
            Groups group = objectMapper.convertValue(messageData.get("body"), Groups.class);
            if (group.getGroupId() == null){
                groupId = groupsService.createGroup(group, group.getCreatorId());
            } else {
                groupId = groupsService.changeGroup(group);
            }
            broadcastToSubscribers("groupService", groupId);
            return;
        }
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
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}