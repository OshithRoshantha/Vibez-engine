package com.vibez.engine.Controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibez.engine.Model.Message;
import com.vibez.engine.Service.MessageService;

@RestController
@RequestMapping("/vibez")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/message/send")
    public  ResponseEntity<Message> sendMessage(@RequestHeader(value = "Authorization", required = true) String token, @RequestBody Message message) {
        return ResponseEntity.ok(messageService.saveMessage(message));
    }
    
    @PutMapping("/message/markAsRead/{messageId}")
    public ResponseEntity<Boolean> markAsRead(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable ObjectId messageId) {
        return ResponseEntity.ok(messageService.markAsRead(messageId));
    }

    @GetMapping("/message/direct/{senderId}/{receiverId}")
    public ResponseEntity<List<Message>> getDirectMessages(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable ObjectId senderId, @PathVariable ObjectId receiverId) {
        return ResponseEntity.ok(messageService.getDirectMessages(senderId, receiverId));
    }

    @GetMapping("/message/group/{groupId}")
    public ResponseEntity<List<Message>> getGroupMessages(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable ObjectId groupId) {
        return ResponseEntity.ok(messageService.getGroupMessages(groupId));
    }
}
