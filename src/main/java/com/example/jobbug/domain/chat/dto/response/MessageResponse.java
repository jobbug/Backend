package com.example.jobbug.domain.chat.dto.response;

import java.time.LocalDateTime;


public record MessageResponse(
    Long messageId,
    String content,
    LocalDateTime timestamp,
    boolean isRead
) {

}
