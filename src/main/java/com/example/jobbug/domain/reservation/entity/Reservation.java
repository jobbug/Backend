package com.example.jobbug.domain.reservation.entity;

import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.post.entity.Post;
import com.example.jobbug.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "address_detail", nullable = false)
    private String addressDetail;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Builder
    public Reservation(Long id, Post post, ChatRoom chatRoom, String address, String addressDetail, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.post = post;
        this.chatRoom = chatRoom;
        this.address = address;
        this.addressDetail = addressDetail;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
