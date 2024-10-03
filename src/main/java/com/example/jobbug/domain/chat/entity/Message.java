package com.example.jobbug.domain.chat.entity;

import com.example.jobbug.domain.chat.entity.firebase.MessageType;
import com.example.jobbug.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "sender_id", nullable = true)
    private Long senderId;

    @Column(name = "isRead", nullable = false)
    private boolean isRead;

    @Builder
    public Message(ChatRoom chatRoom, String content, Long senderId, boolean isRead, MessageType type) {
        this.chatRoom = chatRoom;
        this.content = content;
        this.senderId = senderId;
        this.isRead = isRead;
        this.type = type;
    }
}

