package com.example.jobbug.domain.chat.converter;

import com.example.jobbug.domain.chat.dto.response.MessageResponse;
import com.example.jobbug.domain.chat.entity.Message;
import com.example.jobbug.domain.chat.entity.firebase.FirebaseMessage;
import com.example.jobbug.domain.chat.entity.firebase.MessageType;
import com.example.jobbug.domain.user.entity.User;

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

    public static FirebaseMessage mapToFirebase(User user, Message message, MessageType type) {
        return new FirebaseMessage(
                message.getId(),
                type,
                user.getId(),
                user.getName(),
                message.getContent(),
                message.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli()
        );
    }
}
