package com.skpijtk.springboot_boilerplate.handler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> adminSessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Misalnya client mengirim query param: /ws?role=admin
        String role = session.getUri().getQuery();
        if (role != null && role.contains("role=admin")) {
            adminSessions.add(session);
            session.sendMessage(new TextMessage("Admin connected to WebSocket server"));
        } else {
            session.sendMessage(new TextMessage("Connected to WebSocket server"));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        session.sendMessage(new TextMessage("Echo: " + message.getPayload()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        adminSessions.remove(session);
    }

    public void sendNotificationToAdmins(String message) {
        for (WebSocketSession session : adminSessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
