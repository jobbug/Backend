package com.example.jobbug.global.config.web.socket;

import com.example.jobbug.domain.chat.dto.request.CreateChatRequest;
import com.example.jobbug.domain.chat.dto.request.GetChatRequest;
import com.example.jobbug.domain.chat.dto.response.GetChatResponse;
import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.chat.entity.Message;
import com.example.jobbug.domain.chat.enums.MessageType;
import com.example.jobbug.domain.chat.repository.ChatRoomRepository;
import com.example.jobbug.domain.chat.util.MessageIdGenerator;
import com.example.jobbug.domain.firebase.entity.FirebaseMessage;
import com.example.jobbug.domain.firebase.service.FirebaseService;
import com.example.jobbug.domain.user.entity.User;
import com.example.jobbug.domain.user.repository.UserRepository;
import com.example.jobbug.global.dto.ErrorResponse;
import com.example.jobbug.global.exception.enums.ErrorCode;
import com.example.jobbug.global.exception.model.JobbugException;
import com.example.jobbug.global.exception.model.NotFoundException;
import com.example.jobbug.global.exception.model.UserNotAuthenticatedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MessageWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Set<WebSocketSession> sessions = new HashSet<>();

    private final FirebaseService firebaseService;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    private final MessageIdGenerator messageIdGenerator;

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

        try {
            SocketRequest request = objectMapper.readValue(message.getPayload(), SocketRequest.class);
            String json = objectMapper.writeValueAsString(request.getData());
            System.out.println(json);
            switch (request.getType()) {
                case "chat":
                    handleChatMessage(session, objectMapper.readValue(json, CreateChatRequest.class));
                    break;
                case "message":
                    handleMessage(session, objectMapper.readValue(json, GetChatRequest.class));
                    break;
                default:
                    break;
            }
        } catch (JobbugException e) {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(ErrorResponse.error(e.getErrorCode(), e.getMessage()).getBody())));
        } catch (Exception e) {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(ErrorResponse.error(ErrorCode.INTERNAL_SERVER_EXCEPTION).getBody())));
        }
    }

    private void handleChatMessage(WebSocketSession session, CreateChatRequest request) {
        // 채팅 메세지 처리
        Long userId = (Long) session.getAttributes().get("userId");

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(ErrorCode.NOT_FOUND_USER_EXCEPTION)
        );

        ChatRoom chatRoom = chatRoomRepository.findById(request.getRoomId()).orElseThrow(
                () -> new NotFoundException(ErrorCode.NOT_FOUND_CHATROOM_EXCEPTION)
        );

        long number = messageIdGenerator.nextId();

        Message newMessage = Message.builder()
                .number(number)
                .chatRoom(chatRoom)
                .content(request.getContent())
                .sender(user)
                .isRead(false)
                .type(MessageType.MESSAGE)
                .build();

        firebaseService.sendFirebaseMessage(newMessage, null);
    }

    private void handleMessage(WebSocketSession session, GetChatRequest request) {
        Long userId = (Long) session.getAttributes().get("userId");

        List<FirebaseMessage> messages =  firebaseService.getMessagesByRoomId(request.getRoomId());

        sendMessage(userId, GetChatResponse.builder()
                .type("MESSAGES")
                .messages(messages)
                .build());
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

    private void sendMessage(Long userId, GetChatResponse response) {
        sessions.forEach(session -> {
            try {
                if(session.getAttributes().get("userId").equals(userId)) {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
                }
            } catch (IOException e) {
                e.printStackTrace(); // TODO-HONG : 예외 추가
            }
        });
    }
}
