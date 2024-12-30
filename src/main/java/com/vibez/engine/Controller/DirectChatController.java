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

import com.vibez.engine.Model.DirectChat;
import com.vibez.engine.Service.DirectChatService;

@CrossOrigin(origins = "*" , allowedHeaders = "*")
@RestController
@RequestMapping("/vibez")
public class DirectChatController {
    
    @Autowired
    private DirectChatService directChatService;

    @GetMapping("/directChat/{userId}") //find all direct chats of a user
    public ResponseEntity<List<String>> getDirectChatsByUser(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String userId){
        return ResponseEntity.ok(directChatService.getDirectChatsByUser(userId));
    }

    @GetMapping("/directChat/info/{chatId}") //get direct chat info
    public ResponseEntity<DirectChat> getDirectChatById(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String chatId){
        return ResponseEntity.ok(directChatService.getDirectChatById(chatId));
    }

    @PutMapping("/directChat/favorite/{userId}/{chatId}") //favorite a direct chat
    public ResponseEntity<String> favoriteDirectChat(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String chatId, @PathVariable String userId){
        return ResponseEntity.ok(directChatService.favoriteDirectChat(chatId, userId));
    }
}
