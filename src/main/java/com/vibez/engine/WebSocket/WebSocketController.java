package com.vibez.engine.WebSocket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vibez.engine.Model.DirectChat;
import com.vibez.engine.Model.Friendship;
import com.vibez.engine.Model.GroupAction;
import com.vibez.engine.Model.Groups;
import com.vibez.engine.Model.Marketplace;
import com.vibez.engine.Model.Message;
import com.vibez.engine.Model.MessageInfo;
import com.vibez.engine.Model.RabbitMQ;
import com.vibez.engine.Model.User;
import com.vibez.engine.Model.UserUpdate;
import com.vibez.engine.RabbitMQ.Producer.RabbitMQProducer;
import com.vibez.engine.Repository.DirectChatRepo;
import com.vibez.engine.Repository.FriendshipRepo;
import com.vibez.engine.Repository.GroupRepo;
import com.vibez.engine.Repository.MarketplaceRepo;
import com.vibez.engine.Repository.UserRepo;
import com.vibez.engine.Service.DirectChatService;
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
    private RabbitMQProducer rabbitMQProducer;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupsService groupsService;

    @Autowired
    private DirectChatService directChatService;

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private MarketplaceService marketplaceService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GroupRepo groupRepo;

    @Autowired
    private DirectChatRepo directChatRepo;

    @Autowired
    private MarketplaceRepo MarketplaceRepo;

    @Autowired
    private FriendshipRepo FriendshipRepo;

    @Autowired
    private ObjectMapper objectMapper;

    public static String formatTimestampToHHMM(String timestamp) {
        LocalDateTime dateTime = LocalDateTime.parse(timestamp);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return dateTime.format(formatter);
    }

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
            case "accountDelete":
                handleAccountDelete(messageData);
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

    public void broadcastToSubscribers(String topic, List<String> userIds, Object message) {
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

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consumeMessage(String message) {
        RabbitMQ messageObj = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            messageObj = objectMapper.readValue(message, RabbitMQ.class);
            broadcastToSubscribers("messageService", messageObj.getRelatedIds(), messageObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGroups(Map<String, Object> messageData) {
        String uniqueId = "message_" + System.currentTimeMillis();
        String groupId = null;
        List <String> memberIds = new ArrayList<>();
        if (messageData.get("groupAction") != null){
            GroupAction groupAction = objectMapper.convertValue(messageData.get("groupAction"), GroupAction.class);
            if (groupAction.getAction().equals("addUsers")){
                groupId = groupsService.addUsersToGroup(groupAction.getGroupId(), groupAction.getUserIds());
                memberIds = groupsService.getGroupById(groupId).getMemberIds();
            } else if (groupAction.getAction().equals("removeUsers")){
                memberIds = groupsService.getGroupById(groupAction.getGroupId()).getMemberIds();
                groupId = groupsService.removeUsersFromGroup(groupAction.getGroupId(), groupAction.getUserIds());
            } else if (groupAction.getAction().equals("deleteGroup")){
                memberIds = groupsService.getGroupById(groupAction.getGroupId()).getMemberIds();
                groupId = groupsService.deleteGroup(groupAction.getGroupId());
            }
            Map<String, Object> message = new HashMap<>();
            message.put("action", "groupService");
            message.put("id", uniqueId);
            message.put("groupId", groupId);
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
            List <String> memberIds2 = currentGroup.getMemberIds();
            broadcastToSubscribers("groupService", memberIds2, message);
            return;
        }
    }
    
    private void handleMessages(Map<String, Object> messageData) {
        Message sendMessage = objectMapper.convertValue(messageData.get("body"), Message.class);
        String uniqueId = "message_" + System.currentTimeMillis();
        String updatingId = messageService.sendMessage(sendMessage);
        String messageType = null;
        String senderName = null;
        List <String> relatedIds = new ArrayList<>();
        Map<String, Object> message = new HashMap<>();
        if(sendMessage.getGroupId() != null){
            relatedIds.clear();
            message.put("groupId", updatingId);
            messageType = "group";
            Groups groupToSend = groupsService.getGroupById(updatingId);
            relatedIds.addAll(groupToSend.getMemberIds());
            senderName = userService.getUserById(sendMessage.getSenderId()).getUserName();
        }
        else{
            relatedIds.clear();
            message.put("chatId", updatingId);
            messageType = "direct";
            DirectChat chatToSend = directChatService.getDirectChatById(updatingId);
            relatedIds.addAll(chatToSend.getMemberIds());
        }
        message.put("action", "messageService");
        message.put("id", uniqueId);
        message.put("sender",sendMessage.getSenderId());
        message.put("type", messageType);
        message.put("relatedIds", relatedIds);

        MessageInfo newMessage = new MessageInfo();
        newMessage.setIsSendByMe(true);
        newMessage.setMessage(sendMessage.getMessage());
        newMessage.setTimestamp(formatTimestampToHHMM(sendMessage.getTimestamp().toString()));
        newMessage.setSender(sendMessage.getSenderId());
        newMessage.setSenderName(senderName);

        message.put("payload", newMessage);
        rabbitMQProducer.send(toJson(message));
    }

    private void handleFriendships(Map<String, Object> messageData) {
        Friendship friendship = objectMapper.convertValue(messageData.get("body"), Friendship.class);
        String uniqueId = "message_" + System.currentTimeMillis();
        String friendshipId = null;
        String status = null;
        List <String> relatedIds = new ArrayList<>();
        if (friendship.getFriendshipId() == null) {
            friendshipId = friendshipService.sendFriendRequest(friendship.getUserId(), friendship.getFriendId());
            status = "PENDING";
            relatedIds.add(friendship.getUserId());
            relatedIds.add(friendship.getFriendId()); 
        } else {
            friendshipId = friendship.getFriendshipId();
            Friendship currentFriendship = friendshipService.getFriendship(friendshipId);
            relatedIds.add(currentFriendship.getUserId());
            relatedIds.add(currentFriendship.getFriendId());
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
        broadcastToSubscribers("friendshipService", relatedIds, message);
    }

    private void handleProfileMetaData(Map<String, Object> messageData) {
        List <User> users = userService.getAllUsers();
        List <String> userIds = new ArrayList<>();
        for (User user : users){
            userIds.add(user.getUserId());
        }
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
        broadcastToSubscribers("profileService", userIds, message);
    }

    private void handleMarketplace(Map<String, Object> messageData) {
        List <User> users = userService.getAllUsers();
        List <String> userIds = new ArrayList<>();
        for (User user : users){
            userIds.add(user.getUserId());
        }
        Marketplace product = objectMapper.convertValue(messageData.get("body"), Marketplace.class);
        String uniqeId = "message_" + System.currentTimeMillis();
        String productId = null;
        String productAction = null;

        if (product.getProductAction().equals("ADD")){
            Marketplace newProduct = marketplaceService.addItem(product);
            productId = newProduct.getProductId();
            productAction = "ADDED";
            userIds.clear();
            userIds.add(product.getSellerId());
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
        broadcastToSubscribers("marketplaceService", userIds, message);
    }

    private void handleAccountDelete(Map<String, Object> messageData) {
        List <User> users = userService.getAllUsers();
        List <String> userIds = new ArrayList<>();
        for (User user : users){
            userIds.add(user.getUserId());
        }
        User user = objectMapper.convertValue(messageData.get("body"), User.class);
        Map<String, Object> message = new HashMap<>();
        message.put("action", "accountDelete");
        User existingUser = userRepo.findByUserId(user.getUserId());
        userRepo.delete(existingUser);
        List <String> relatedIds = new ArrayList<>();
        List <Marketplace> products = MarketplaceRepo.findBySellerId(user.getUserId());
        List <String> directChats = existingUser.getDirectChatIds();
        List <String> groups = existingUser.getGroupIds();
        List <String> friendships = friendshipService.getLinkedProfiles(user.getUserId());

        if(friendships != null){
            String uniqeId = "message_" + System.currentTimeMillis();
            message.put("id", uniqeId);
            for (String friendshipId : friendships) {
                Friendship friendship = friendshipService.getFriendship(friendshipId);
                relatedIds.add(friendship.getUserId());
                relatedIds.add(friendship.getFriendId());
                FriendshipRepo.deleteById(friendshipId);
            }
            message.put("typeOfAction", "friendship");
            broadcastToSubscribers("accountDelete", relatedIds, message);
        }
        if(directChats != null){
            String uniqeId = "message_" + System.currentTimeMillis();
            message.put("id", uniqeId);
            for (String directChatId : directChats) {
                DirectChat  directChat = directChatRepo.findByChatId(directChatId);
                List <String> messageIds = directChat.getMessageIds();
                for (String messageId : messageIds) {
                    Message messageToDelete = messageService.getMessage(messageId);
                    messageToDelete.setRead(true);
                }
                List <String> memberIds = directChat.getMemberIds();
                String otherUser = memberIds.stream()
                                        .filter(id -> !id.equals(user.getUserId()))
                                        .findFirst()
                                        .orElse(null); 
                User otherUserObj = userRepo.findByUserId(otherUser);
                otherUserObj.getDirectChatIds().remove(directChatId);
                userRepo.save(otherUserObj);
                relatedIds.add(otherUser);
                directChatRepo.deleteByChatId(directChatId);
            }
            message.put("typeOfAction", "directChat");
            broadcastToSubscribers("accountDelete", relatedIds, message);
        }
        if (groups != null){
            String uniqeId = "message_" + System.currentTimeMillis();
            message.put("id", uniqeId);
            List <String> groupIds = new ArrayList<>();
            List <String> allMembers = new ArrayList<>();
            List <String> deletedGroups = new ArrayList<>();
            for (String groupId : groups) {
                Groups group = groupRepo.findByGroupId(groupId);
                if (group.getCreatorId().equals(user.getUserId())){
                    deletedGroups.add(groupId);
                    groupRepo.deleteByGroupId(groupId);
                }
                groupIds.add(groupId);
                allMembers.addAll(group.getMemberIds());
                group.getMemberIds().remove(user.getUserId());
                groupRepo.save(group);
            }
            message.put("typeOfAction", "groupChat");
            message.put("groupIds", groupIds);
            message.put("deletedGroups", deletedGroups);
            broadcastToSubscribers("accountDelete", allMembers, message);
        }
        if (products != null){
            String uniqeId = "message_" + System.currentTimeMillis();
            message.put("id", uniqeId);
            List <String> productIds = new ArrayList<>();
            for (Marketplace product : products) {
                productIds.add(product.getProductId());
                MarketplaceRepo.deleteById(product.getProductId());
            }
            message.put("typeOfAction", "marketplace");
            message.put("productId", productIds);
            broadcastToSubscribers("accountDelete", userIds, message);
        }
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

    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}