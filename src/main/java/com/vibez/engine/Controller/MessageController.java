package com.vibez.engine.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

@CrossOrigin(origins = "*" , allowedHeaders = "*")
@RestController
@RequestMapping("/vibez")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/message/send")
    public  ResponseEntity<String> sendMessage(@RequestHeader(value = "Authorization", required = true) String token, @RequestBody Message message) {
        return ResponseEntity.ok(messageService.sendMessage(message));
    }
    
    @PutMapping("/message/markAsRead/{messageId}")
    public ResponseEntity<Boolean> markAsRead(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String messageId) {
        return ResponseEntity.ok(messageService.markAsRead(messageId));
    }

    @GetMapping("/message/group/{groupId}") //find all messages in a group
    public ResponseEntity<List<String>> getMessagesByGroups(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String groupId){
        return ResponseEntity.ok(messageService.getMessagesByGroups(groupId));
    }

    @GetMapping("/message/directChat/{directChatId}") //find all messages in a direct chat
    public ResponseEntity<List<String>> getMessagesByDirectChat(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String directChatId){
        return ResponseEntity.ok(messageService.getMessagesByDirectChat(directChatId));
    }

    @GetMapping("/message/{messageId}") //get message info
    public ResponseEntity<Message> getMessage(@RequestHeader(value = "Authorization", required = true) String token, @PathVariable String messageId) {
        return ResponseEntity.ok(messageService.getMessage(messageId));
    }
}