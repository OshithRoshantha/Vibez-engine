package com.vibez.engine.Service;

import java.util.List;

import com.vibez.engine.Model.DirectChat;
import com.vibez.engine.Model.Receipt;

public interface  DirectChatService {
    List<String> getDirectChatsByUser(String userId);
    String createDirectChat(String userId1, String userId2);
    boolean isAvailableDirectChat(String userId1, String userId2);
    DirectChat getDirectChatById(String chatId);
    String favoriteDirectChat(String chatId, String userId);
    Receipt getReceipt(String userId, String directChatId);
}
