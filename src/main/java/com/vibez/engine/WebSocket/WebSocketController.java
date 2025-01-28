package com.vibez.engine.WebSocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
import com.vibez.engine.Model.Marketplace;
import com.vibez.engine.Model.Message;
import com.vibez.engine.Model.User;
import com.vibez.engine.Model.UserUpdate;
import com.vibez.engine.Service.FriendshipService;
import com.vibez.engine.Service.GroupsService;
import com.vibez.engine.Service.MarketplaceService;
import com.vibez.engine.Service.MessageService;
import com.vibez.engine.Service.UserService;


public class WebSocketController implements WebSocketHandler {

    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private Map<String, List<WebSocketSession>> subscriptions = new HashMap<>();
    

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupsService groupsService;

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private MarketplaceService marketplaceService;

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
            case "profileService":
                handleProfileMetaData(messageData);
                break;
            case "marketplaceService":
                handleMarketplace(messageData);
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
        String userId = (String) messageData.get("userId");
        String topicWithUserId = topic + "_" + userId;
        subscriptions.computeIfAbsent(topicWithUserId, k -> new ArrayList<>()).add(session);
    }


    private void broadcastToSubscribers(String topic, List<String> userIds, Object message) {
        for (String userId : userIds) {
            String topicWithUserId = topic + "_" + userId;
            List<WebSocketSession> subscribers = subscriptions.get(topicWithUserId);
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
    }

    private void handleGroups(Map<String, Object> messageData) {
        String uniqueId = "message_" + System.currentTimeMillis();
        String groupId = null;
        if (messageData.get("groupAction") != null){
            GroupAction groupAction = objectMapper.convertValue(messageData.get("groupAction"), GroupAction.class);
            if (groupAction.getAction().equals("addUsers")){
                groupId = groupsService.addUsersToGroup(groupAction.getGroupId(), groupAction.getUserIds());
            } else if (groupAction.getAction().equals("removeUsers")){
                groupId = groupsService.removeUsersFromGroup(groupAction.getGroupId(), groupAction.getUserIds());
            } else if (groupAction.getAction().equals("deleteGroup")){
                groupId = groupsService.deleteGroup(groupAction.getGroupId());
            }
            Map<String, Object> message = new HashMap<>();
            message.put("action", "groupService");
            message.put("id", uniqueId);
            message.put("groupId", groupId);
            Groups currentGroup = groupsService.getGroupById(groupId);
            List <String> memberIds = currentGroup.getMemberIds();
            broadcastToSubscribers("groupService", memberIds, message);
            return;
        }
        if (messageData.get("body") != null){
            Groups group = objectMapper.convertValue(messageData.get("body"), Groups.class);
            if (group.getGroupId() == null){
                groupId = groupsService.createGroup(group, group.getCreatorId());
            } else {
                groupId = groupsService.changeGroup(group);
            }
            Map<String, Object> message = new HashMap<>();
            message.put("action", "groupService");
            message.put("id", uniqueId);
            message.put("groupId", groupId);
            Groups currentGroup = groupsService.getGroupById(groupId);
            List <String> memberIds = currentGroup.getMemberIds();
            broadcastToSubscribers("groupService", memberIds, message);
            return;
        }
    }
    
    private void handleMessages(Map<String, Object> messageData) {
        Message sendMessage = objectMapper.convertValue(messageData.get("body"), Message.class);
        String uniqueId = "message_" + System.currentTimeMillis();
        String updatingId = messageService.sendMessage(sendMessage);
        String messageType = null;
        List <String> relatedIds = new ArrayList<>();
        Map<String, Object> message = new HashMap<>();
        if(sendMessage.getGroupId() != null){
            message.put("groupId", updatingId);
            messageType = "group";
            Groups group = groupsService.getGroupById(updatingId);
            relatedIds.addAll(group.getMemberIds());
        }
        else{
            message.put("chatId", updatingId);
            messageType = "direct";
            Message messageToSend = messageService.getMessage(updatingId);
            relatedIds.add(messageToSend.getSenderId());
            relatedIds.add(messageToSend.getReceiverId());
        }
        message.put("action", "messageService");
        message.put("id", uniqueId);
        message.put("sender",sendMessage.getSenderId());
        message.put("type", messageType);
        broadcastToSubscribers("messageService", relatedIds, message);
    }

    private void handleFriendships(Map<String, Object> messageData) {
        Friendship friendship = objectMapper.convertValue(messageData.get("body"), Friendship.class);
        String uniqueId = "message_" + System.currentTimeMillis();
        String friendshipId = null;
        String status = null;
        if (friendship.getFriendshipId() == null) {
            friendshipId = friendshipService.sendFriendRequest(friendship.getUserId(), friendship.getFriendId());
            status = "PENDING";
        } else {
            friendshipId = friendship.getFriendshipId();
            if (friendship.getStatus().equals("ACCEPTED")) {
                friendshipService.acceptFriendRequest(friendship.getFriendshipId());
                status = "ACCEPTED";
            } else if (friendship.getStatus().equals("UNFRIENDED")) {
                friendshipService.unFriend(friendship.getFriendshipId());
                status = "UNFRIENDED";
            } else if (friendship.getStatus().equals("BLOCKED")) {
                friendshipService.blockFriend(friendship.getFriendshipId());
                status = "BLOCKED";
            }
        }
        Map<String, Object> message = new HashMap<>();
        message.put("action", "friendshipService");
        message.put("id", uniqueId);
        message.put("friendshipId", friendshipId);
        message.put("status", status);
        broadcastToSubscribers("friendshipService", message);
    }

    private void handleProfileMetaData(Map<String, Object> messageData) {
        UserUpdate userUpdate = objectMapper.convertValue(messageData.get("body"), UserUpdate.class);
        String uniqueId = "message_" + System.currentTimeMillis();
        User existingUser = userService.getUserById(userUpdate.getUserId());
        if (userUpdate.getUserName() != null) {
            existingUser.setUserName(userUpdate.getUserName());
        }
        if (userUpdate.getAbout() != null) {
            existingUser.setAbout(userUpdate.getAbout());
        }
        if (userUpdate.getProfilePicture() != null) {
            existingUser.setProfilePicture(userUpdate.getProfilePicture());
        }
        String userId = userService.updateProfile(existingUser);

        Map<String, Object> message = new HashMap<>();
        message.put("id", uniqueId);
        message.put("action", "profileService");
        message.put("body", userId);
        broadcastToSubscribers("profileService", message);
    }

    private void handleMarketplace(Map<String, Object> messageData) {
        Marketplace product = objectMapper.convertValue(messageData.get("body"), Marketplace.class);
        System.out.println(product);
        String uniqeId = "message_" + System.currentTimeMillis();
        String productId = null;
        String productAction = null;

        if (product.getProductAction().equals("ADD")){
            Marketplace newProduct = marketplaceService.addItem(product);
            productId = newProduct.getProductId();
            productAction = "ADDED";
        }
        else if (product.getProductAction().equals("UPDATE")){
            Marketplace updatedProduct = marketplaceService.updateItem(product);
            productId = updatedProduct.getProductId();
            productAction = "UPDATED";
        }
        else if (product.getProductAction().equals("REMOVE")){
            productId = marketplaceService.deleteItem(product.getProductId());
            productAction = "REMOVED";
        }

        Map<String, Object> message = new HashMap<>();
        message.put("id", uniqeId);
        message.put("action", "marketplaceService");
        message.put("body", productId);
        message.put("productAction", productAction);
        broadcastToSubscribers("marketplaceService", message);
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