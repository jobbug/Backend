package com.example.jobbug.domain.chat.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class MessageResponse {
    private Long messageId;
    private String content;
    private Long senderId;
    private LocalDateTime timestamp;
    private boolean isRead;


}


