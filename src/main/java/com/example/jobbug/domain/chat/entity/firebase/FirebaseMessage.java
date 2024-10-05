package com.example.jobbug.domain.chat.entity.firebase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FirebaseMessage {
    private Long number;
    private MessageType type;
    private Long senderId;
    private Long roomId;
    private String senderName;
    private String content;
    private Long timestamp;
}

