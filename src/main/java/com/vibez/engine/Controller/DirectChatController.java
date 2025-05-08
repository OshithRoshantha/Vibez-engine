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
import com.vibez.engine.Model.DirectChatPreview;
import com.vibez.engine.Service.DirectChatService;

//@CrossOrigin(origins = "*" , allowedHeaders = "*")
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/vibez")
public class DirectChatController {
    
    @Autowired
    private DirectChatService directChatService;

    @GetMapping("/directChat/info/{chatId}") //get direct chat info
    public ResponseEntity<DirectChat> getDirectChatById(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String chatId){
        return ResponseEntity.ok(directChatService.getDirectChatById(chatId));
    }
    
    @GetMapping("/directChat/preview/{userId}/{chatId}") //get direct chat preview
    public ResponseEntity<DirectChatPreview> getDirectChatPreview(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String userId, @PathVariable String chatId){
        return ResponseEntity.ok(directChatService.getDirectChatPreview(chatId, userId));
    }

    @PutMapping("/directChat/favorite/{userId}/{chatId}") //favorite a direct chat
    public ResponseEntity<String> favoriteDirectChat(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String chatId, @PathVariable String userId){
        return ResponseEntity.ok(directChatService.favoriteDirectChat(chatId, userId));
    }

    @PutMapping("/directChat/unfavorite/{userId}/{chatId}") //unfavorite a direct chat
    public ResponseEntity<String> unfavoriteDirectChat(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String chatId, @PathVariable String userId){
        return ResponseEntity.ok(directChatService.unfavoriteDirectChat(chatId, userId));
    }

    @GetMapping("/directChat/isRelated/{userId}/{chatId}") //check if a user is related to a direct chat
    public ResponseEntity<Boolean> isUserRelatedToDirectChat(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String chatId, @PathVariable String userId){
        return ResponseEntity.ok(directChatService.isUserRelatedToDirectChat(chatId, userId));
    }

    @GetMapping("/directChat/find/{userId}/{keyword}") //find direct chat by keyword
    public ResponseEntity <List<String>> findDirectChatByKeyword(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String keyword, @PathVariable String userId){
        return ResponseEntity.ok(directChatService.findDirectChat(keyword, userId));
    }

}
