package com.vibez.engine.Service;

import java.util.List;

import com.vibez.engine.Model.DirectChat;
import com.vibez.engine.Model.DirectChatPreview;

public interface  DirectChatService {
    List<String> getDirectChatsByUser(String userId);
    String createDirectChat(String userId1, String userId2);
    String isAvailableDirectChat(String userId1, String userId2);
    DirectChat getDirectChatById(String chatId);
    String favoriteDirectChat(String chatId, String userId);
    String unfavoriteDirectChat(String chatId, String userId);
    DirectChatPreview getDirectChatPreview(String chatId, String userId);
}
