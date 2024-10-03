package com.example.jobbug.domain.chat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateChatRequest {
    private final Long roomId;
    private final String content;
}