package com.example.jobbug.domain.review.entity;

import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.post.entity.Post;
import com.example.jobbug.domain.review.dto.request.SaveReviewRequest;
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
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom roomId;

    @Column(name = "late_time", nullable = false)
    private int lateTime;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "point", nullable = false)
    private int point;

    @Column(name = "isArrive", nullable = false)
    private boolean isArrive;

    @Column(name = "isSuccess", nullable = false)
    private char isSuccess;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Builder
    public Review(User writer, User author, ChatRoom roomId, Post post, int lateTime, String content, int point, boolean isArrive, char isSuccess) {
        this.writer = writer;
        this.author = author;
        this.roomId = roomId;
        this.lateTime = lateTime;
        this.content = content;
        this.point = point;
        this.isArrive = isArrive;
        this.isSuccess = isSuccess;
        this.post = post;
    }

    public static Review of(User writer, ChatRoom chatRoom, Post post, SaveReviewRequest request){
        return Review.builder()
                .writer(writer)
                .author(chatRoom.getParticipant())
                .roomId(chatRoom)
                .post(post)
                .lateTime(request.getLateTime())
                .content(request.getContent())
                .point(request.getPoint())
                .isArrive(request.isArrive())
                .isSuccess(request.getIsSuccess())
                .build();
    }
}

