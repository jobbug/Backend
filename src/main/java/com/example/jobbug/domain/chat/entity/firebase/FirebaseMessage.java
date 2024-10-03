package com.example.jobbug.domain.chat.entity.firebase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class FirebaseMessage {
    private Long number;
    private MessageType type;
    private Long senderId;
    private String senderName;
    private String content;
    private Long timestamp;
}

