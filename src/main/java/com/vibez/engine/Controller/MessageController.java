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

import com.vibez.engine.Model.Message;
import com.vibez.engine.Model.MessageInfo;
import com.vibez.engine.Service.MessageService;

@CrossOrigin(origins = "*" , allowedHeaders = "*")
@RestController
@RequestMapping("/vibez")
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

    @GetMapping("/message/directChat/{userId}/{reciverId}") //find all messages in a direct chat
    public ResponseEntity<List<MessageInfo>> getMessagesByDirectChat(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String userId, @PathVariable String reciverId){
        return ResponseEntity.ok(messageService.getMessagesByDirectChat(userId, reciverId));
    }

    @GetMapping("/message/{messageId}") //get message info
    public ResponseEntity<Message> getMessage(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String messageId) {
        return ResponseEntity.ok(messageService.getMessage(messageId));
    }
}