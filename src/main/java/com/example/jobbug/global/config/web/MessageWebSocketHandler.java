package com.example.jobbug.global.config.web;

import com.example.jobbug.domain.firebase.entity.FirebaseMessage;
import com.example.jobbug.global.exception.enums.ErrorCode;
import com.example.jobbug.global.exception.model.UserNotAuthenticatedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class MessageWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Authentication authentication = (Authentication) session.getAttributes().get("Authentication");
        if (authentication == null) {
            throw new UserNotAuthenticatedException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        Long userId = Long.valueOf(authentication.getPrincipal().toString());
        session.getAttributes().put("userId", userId);
        sessions.add(session);

        session.sendMessage(
                new TextMessage("웹소켓 연결 성공"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        // 클라이언트에서 받은 메세지
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    public void sendMessage(Long userId, FirebaseMessage message) {
        sessions.forEach(session -> {
            try {
                if(session.getAttributes().get("userId").equals(userId)) {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
                }
            } catch (IOException e) {
                e.printStackTrace(); // TODO-HONG : 예외 추가
            }
        });
    }
}
