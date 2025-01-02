package com.vibez.engine.WebSocket;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.vibez.engine.Service.JwtUtil;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String token = request.getURI().getQuery();  
        if (token != null && token.startsWith("token=")) {
            token = token.substring(6);  
            if (jwtUtil.isTokenValid(token)) {
                String email = jwtUtil.extractEmail(token);
                attributes.put("userEmail", email);
                return true;
            }
        }
        throw new HandshakeFailureException("Invalid JWT token");
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        
    }
}
