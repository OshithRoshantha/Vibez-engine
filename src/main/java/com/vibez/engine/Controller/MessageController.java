package com.vibez.engine.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibez.engine.Model.GroupMessageInfo;
import com.vibez.engine.Model.Message;
import com.vibez.engine.Model.MessageInfo;
import com.vibez.engine.Service.MessageService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/vibez")
//@CrossOrigin(origins = "http://localhost:5173")
public class MessageController {

    @Autowired
    private MessageService messageService;
    
    @PutMapping("/message/markAsRead/{userId}/{receiverId}")
    public ResponseEntity<Void> markAsRead(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId, @PathVariable String receiverId) {
        messageService.markAsRead(userId, receiverId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/message/unReadCount/{userId}")
    public ResponseEntity<Integer> getUnReadCount(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId){
        return ResponseEntity.ok(messageService.getUnReadCount(userId));
    }

    @GetMapping("/message/group/{groupId}") //find all messages in a group
    public ResponseEntity<List<String>> getMessagesByGroups(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String groupId){
        return ResponseEntity.ok(messageService.getMessagesByGroups(groupId));
    }

    @GetMapping("/message/directChat/{userId}/{reciverId}") 
    public ResponseEntity<List<MessageInfo>> getMessagesByDirectChat(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId, @PathVariable String reciverId){
        return ResponseEntity.ok(messageService.getMessagesByDirectChat(userId, reciverId));
    }

    @GetMapping("/message/groupChat/{userId}/{groupId}") 
    public ResponseEntity<List<GroupMessageInfo>> getMessagesByGroupChat(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId, @PathVariable String groupId){
        return ResponseEntity.ok(messageService.getMessagesByGroupChat(userId, groupId));
    }

    @GetMapping("/message/{messageId}") //get message info
    public ResponseEntity<Message> getMessage(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String messageId) {
        return ResponseEntity.ok(messageService.getMessage(messageId));
    }

    @GetMapping("/messages/checkUnread/{chatId}/{userId}") //check if a direct chat has unread messages
    public ResponseEntity<Boolean> checkUnreadMessages(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String chatId, @PathVariable String userId){
        return ResponseEntity.ok(messageService.checkUnreadMessages(chatId, userId));
    }

    @GetMapping("/messages/checkUnreadGroup/{groupId}/{userId}") //check if a group chat has unread messages
    public ResponseEntity<Boolean> checkUnreadGroupMessages(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId, @PathVariable String groupId){
        return ResponseEntity.ok(messageService.checkUnreadGroupMessages(groupId, userId));
    }

    @GetMapping("/message/unreadGroup/{userId}") //count unread group messages
    public ResponseEntity<Integer> unreadMessageCount(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId){
        return ResponseEntity.ok(messageService.unreadGroupMessageCount(userId));
    }

    @PutMapping("/message/markAsReadGroup/{userId}/{groupId}") //mark group messages as read
    public ResponseEntity<Void> markAsReadGroup(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId, @PathVariable String groupId){
        messageService.markAsReadGroup(userId, groupId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/message/history/{userId}/{receiverId}") //get message history
    public ResponseEntity<List<String>> getMessageHistory(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId, @PathVariable String receiverId){
        return ResponseEntity.ok(messageService.getMessageHistory(userId, receiverId));
    }
}