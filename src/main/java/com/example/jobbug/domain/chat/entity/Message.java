package com.example.jobbug.domain.chat.entity;

import com.example.jobbug.domain.chat.enums.MessageType;
import com.example.jobbug.domain.user.entity.User;
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

    @Column(name = "number", nullable = false)
    private Long number;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = true)
    private User sender;

    @Column(name = "isRead", nullable = false)
    private boolean isRead;

    @Builder
    public Message(Long number, ChatRoom chatRoom, String content, User sender, boolean isRead, MessageType type) {
        this.number = number;
        this.chatRoom = chatRoom;
        this.content = content;
        this.sender = sender;
        this.isRead = isRead;
        this.type = type;
    }
}

