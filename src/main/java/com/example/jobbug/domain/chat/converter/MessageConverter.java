package com.example.jobbug.domain.chat.converter;

import com.example.jobbug.domain.chat.dto.response.MessageResponse;
import com.example.jobbug.domain.chat.entity.Message;
import com.example.jobbug.domain.firebase.entity.FirebaseMessage;
import com.example.jobbug.domain.chat.enums.MessageType;
import com.example.jobbug.domain.firebase.entity.FirebaseMessageData;
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

    public static FirebaseMessage mapToFirebase(Message message, FirebaseMessageData data) {
        User sender = message.getSender();

        return new FirebaseMessage(
                message.getNumber(),
                message.getType(),
                getSenderId(sender),
                message.getChatRoom().getId(),
                getSenderNickName(sender),
                message.getContent(),
                message.isRead(),
                LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli(),
                data
        );
    }

    private static Long getSenderId(User sender) {
        if(sender == null) {
            return null;
        }
        return sender.getId();
    }

    private static String getSenderNickName(User sender) {
        if(sender == null) {
            return null;
        }
        return sender.getNickname();
    }
}
