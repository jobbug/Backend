package com.example.jobbug.domain.chat.entity;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "isRead", nullable = false)
    private boolean isRead;

    @Builder
    public Message(ChatRoom chatRoom, String content, Long senderId, boolean isRead) {
        this.chatRoom = chatRoom;
        this.content = content;
        this.senderId = senderId;
        this.isRead = isRead;
    }
}

