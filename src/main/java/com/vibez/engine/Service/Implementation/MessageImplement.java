package com.vibez.engine.Service.Implementation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.DirectChat;
import com.vibez.engine.Model.GroupMessageInfo;
import com.vibez.engine.Model.Groups;
import com.vibez.engine.Model.Message;
import com.vibez.engine.Model.MessageInfo;
import com.vibez.engine.Model.User;
import com.vibez.engine.Repository.DirectChatRepo;
import com.vibez.engine.Repository.GroupRepo;
import com.vibez.engine.Repository.MessageRepo;
import com.vibez.engine.Repository.UserRepo;
import com.vibez.engine.Service.DirectChatService;
import com.vibez.engine.Service.GroupsService;
import com.vibez.engine.Service.MessageService;
import com.vibez.engine.Service.UserService;

@Service
public class MessageImplement implements MessageService {

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private  GroupsService groupsService;

    @Autowired
    private  UserService userService;

    @Autowired
    private  GroupRepo groupRepo;

    @Autowired
    private UserRepo UserRepo;

    @Autowired
    private  DirectChatService directChatService;

    @Autowired
    private DirectChatRepo directChatRepo;

    public String sendMessage(Message message){
        message.setTimestamp(LocalDateTime.now());
        if (message.getGroupId() != null) {
            List <String> readBy = new ArrayList<>();
            readBy.add(message.getSenderId());
            message.setReadBy(readBy);
        }
        else{
            message.setRead(false);
        }
        message = messageRepo.save(message);
        String updatingId = null;

        if (message.getGroupId() != null){
            Groups group = groupsService.getGroupById(message.getGroupId());
            group.setLastUpdate(LocalDateTime.now());
            group.setLastMessage(message.getMessage());
            User sender = userService.getUserById(message.getSenderId());
            group.setLastMessageSender(sender.getUserName());
            if (group.getMessageIds() == null){
                group.setMessageIds(new ArrayList<>());
            }
            group.getMessageIds().add(message.getMessageId());
            groupRepo.save(group);
            updatingId = group.getGroupId();
            return updatingId;

        }
        else {
            String chatId = directChatService.isAvailableDirectChat(message.getSenderId(), message.getReceiverId());
            if (chatId.equals("null")){
                updatingId = directChatService.createDirectChat(message.getSenderId(), message.getReceiverId());
                DirectChat directChat = directChatService.getDirectChatById(updatingId);
                directChat.setLastUpdate(LocalDateTime.now());
                directChat.setLastMessage(message.getMessage());
                User sender = userService.getUserById(message.getSenderId());
                directChat.setLastMessageSender(sender.getUserName());
                if (directChat.getMessageIds() == null){
                    directChat.setMessageIds(new ArrayList<>());
                }
                directChat.getMessageIds().add(message.getMessageId());
                directChatRepo.save(directChat);
                updatingId = directChat.getChatId();
                return updatingId;
            }
            else {
                DirectChat directChat = directChatService.getDirectChatById(chatId);

                User user1 = userService.getUserById(message.getSenderId());
                User user2 = userService.getUserById(message.getReceiverId());
                if (!user1.getDirectChatIds().contains(chatId)){
                    user1.getDirectChatIds().add(chatId);
                    UserRepo.save(user1);
                }
                if (!user2.getDirectChatIds().contains(chatId)){
                    user2.getDirectChatIds().add(chatId);
                    UserRepo.save(user2);
                }

                directChat.setLastUpdate(LocalDateTime.now());
                directChat.setLastMessage(message.getMessage());
                User sender = userService.getUserById(message.getSenderId());
                directChat.setLastMessageSender(sender.getUserName());
                if (directChat.getMessageIds() == null){
                    directChat.setMessageIds(new ArrayList<>());
                }
                directChat.getMessageIds().add(message.getMessageId());
                directChatRepo.save(directChat);
                updatingId = directChat.getChatId();
                return updatingId;
            }
        }
    }

    public void markAsRead(String userId, String receiverId){
        List <Message> messages = messageRepo.findBySenderIdAndReceiverId(userId, receiverId);
        for (Message message : messages){
            message.setRead(true);
            messageRepo.save(message);
        }
    }

    public void markAsReadGroup(String userId, String groupId){
        Groups group = groupsService.getGroupById(groupId);
        List <String> messageIds = group.getMessageIds();
        if (messageIds == null){
            return;
        }
        for (String messageId : messageIds){
            Message message = messageRepo.findByMessageId(messageId);
            if (!message.getReadBy().contains(userId)){
                message.getReadBy().add(userId);
                messageRepo.save(message);
            }
        }
    }

    public int getUnReadCount(String userId){
        List <Message> messages = messageRepo.findByReceiverId(userId);
        int count = 0;
        for (Message message : messages){
            if (!message.isRead()){
                count++;
            }
        }
        return count;
    }

    public List<String> getMessagesByGroups(String groupId){
        List <Message> messages = messageRepo.findByGroupId(groupId);
        List<String> messageIds = new ArrayList<>();
        if (messages == null){
            return null;
        }
        for (Message message : messages){
            messageIds.add(message.getMessageId());
        }
        return messageIds;
    }

    public int unreadGroupMessageCount(String userId){
        User user = userService.getUserById(userId);
        List <String> groups = user.getGroupIds();
        if (groups == null){
            return 0;
        }
        int count = 0;
        for (String groupId : groups){
            Groups group = groupsService.getGroupById(groupId);
            List <String> messageIds = group.getMessageIds();
            if(messageIds == null){
                continue;
            }
            for (String messageId : messageIds){
                Message message = messageRepo.findByMessageId(messageId);
                if (message.getReadBy() != null){
                    if (!message.getReadBy().contains(userId)){
                        count++;
                    }
                }
            }
        }
        return count;
    }


    public List<MessageInfo> getMessagesByDirectChat(String userId, String reciverId){
        String directChatId = directChatService.isAvailableDirectChat(userId, reciverId);
        if (directChatId.equals("null")){
            return null;
        }
        DirectChat directChat = directChatService.getDirectChatById(directChatId);
        List<String> messageIds = directChat.getMessageIds();
        List <Message> messages = new ArrayList<>();
        for (String messageId : messageIds){
            messages.add(messageRepo.findByMessageId(messageId));
        }
        List <MessageInfo> messageInfos = new ArrayList<>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        for (Message message : messages){
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage(message.getMessage());
            String formattedTimestamp = message.getTimestamp().format(timeFormatter);
            messageInfo.setTimestamp(formattedTimestamp);
            if (message.getSenderId().equals(userId)){
                messageInfo.setIsSendByMe(true);
            }
            else {
                messageInfo.setIsSendByMe(false);
            }
            messageInfos.add(messageInfo);
        }
        return messageInfos;
    }

    public List<GroupMessageInfo> getMessagesByGroupChat(String userId, String groupId){
        Groups group = groupsService.getGroupById(groupId);
        if (group == null){
            return null;
        }
        List<String> messageIds = group.getMessageIds();
        if (messageIds == null){
            return null;
        }
        List <Message> messages = new ArrayList<>();
        for (String messageId : messageIds){
            messages.add(messageRepo.findByMessageId(messageId));
        }
        List <GroupMessageInfo> messageInfos = new ArrayList<>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        for (Message message : messages){
            GroupMessageInfo messageInfo = new GroupMessageInfo();
            messageInfo.setMessage(message.getMessage());
            messageInfo.setSender(userService.getUserById(message.getSenderId()).getUserName());
            String formattedTimestamp = message.getTimestamp().format(timeFormatter);
            messageInfo.setTimestamp(formattedTimestamp);
            if (message.getSenderId().equals(userId)){
                messageInfo.setIsSendByMe(true);
            }
            else {
                messageInfo.setIsSendByMe(false);
            }
            messageInfos.add(messageInfo);
        }
        return messageInfos;
    }

    public Message getMessage(String messageId){
        return messageRepo.findByMessageId(messageId);
    }

    public Boolean checkUnreadMessages(String chatId, String userId) {
        DirectChat chat = directChatRepo.findByChatId(chatId);
        List <String> messageIds = chat.getMessageIds();
        for(String messageId : messageIds){
            Message message = messageRepo.findByMessageId(messageId);
            if(message.getReceiverId().equals(userId) && !message.isRead()){
                return true;
            }
        }
        return false;
    }

    public Boolean checkUnreadGroupMessages(String groupId, String userId) {
        Groups group = groupRepo.findByGroupId(groupId);
        List <String> messageIds = group.getMessageIds();
        if (messageIds == null){
            return false;
        }
        for(String messageId : messageIds){
            Message message = messageRepo.findByMessageId(messageId);
            if(!message.getReadBy().contains(userId)){
                return true;
            }
        }
        return false;
    }

    public List<String> getMessageHistory(String userId, String reciverId){
        String directChatId = directChatService.isAvailableDirectChat(userId, reciverId);
        DirectChat directChat = directChatService.getDirectChatById(directChatId);
        List<String> messageIds = directChat.getMessageIds();
        List <Message> messages = new ArrayList<>();
        for (String messageId : messageIds){
            messages.add(messageRepo.findByMessageId(messageId));
        }
        List <String> messageHistory = new ArrayList<>();
        for (Message message : messages){
            messageHistory.add(message.getMessage());
        }
        return messageHistory;
    }
}
