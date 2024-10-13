package com.example.jobbug.domain.firebase.entity;

import com.example.jobbug.domain.chat.enums.MessageType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirebaseMessage {
    private Long number;
    private MessageType type;
    private Long senderId;
    private Long roomId;
    private String senderName;
    private String content;
    private boolean isRead;
    private Long timestamp;
    private FirebaseMessageData data;
}

