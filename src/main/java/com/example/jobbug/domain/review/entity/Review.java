package com.example.jobbug.domain.review.entity;

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

    @Column(name = "room_id", nullable = false)
    private Long roomId;

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

    @Builder
    public Review(User writer, User author, Long roomId, int lateTime, String content, int point, boolean isArrive, char isSuccess) {
        this.writer = writer;
        this.author = author;
        this.roomId = roomId;
        this.lateTime = lateTime;
        this.content = content;
        this.point = point;
        this.isArrive = isArrive;
        this.isSuccess = isSuccess;
    }
}
