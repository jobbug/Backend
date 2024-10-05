package com.example.jobbug.domain.chat.converter;

import com.example.jobbug.domain.chat.dto.response.MessageResponse;
import com.example.jobbug.domain.chat.entity.Message;
import com.example.jobbug.domain.chat.entity.firebase.FirebaseMessage;
import com.example.jobbug.domain.chat.entity.firebase.MessageType;
import com.example.jobbug.domain.user.entity.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class MessageConverter {

    public static MessageResponse mapToResponse(Message message) {
        if(message == null) {
            return null;
        }
        return new MessageResponse(
                message.getId(),
                message.getContent(),
                message.getCreatedAt(),
                message.isRead()
        );
    }

    public static FirebaseMessage mapToFirebase(User user, Message message, Long roomId, MessageType type) {
        return new FirebaseMessage(
                message.getNumber(),
                type,
                user.getId(),
                roomId,
                user.getName(),
                message.getContent(),
                LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() // TODO-HONG : 추후 시간 일치 필요
        );
    }
}