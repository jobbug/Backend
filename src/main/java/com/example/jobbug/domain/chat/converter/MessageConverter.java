package com.example.jobbug.domain.chat.converter;

import com.example.jobbug.domain.chat.dto.response.MessageResponse;
import com.example.jobbug.domain.chat.entity.Message;

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
}
