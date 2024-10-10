package com.example.jobbug.domain.post.entity;

import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.post.dto.request.SavePostRequest;
import com.example.jobbug.domain.post.enums.PostStatus;
import com.example.jobbug.domain.reservation.entity.ChatRoomStatus;
import com.example.jobbug.domain.reservation.entity.Reservation;
import com.example.jobbug.domain.user.entity.User;
import com.example.jobbug.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "bug_type", nullable = false)
    private String bugType;

    @Column(name = "bug_name")
    private String bugName;

    @Column(name = "origin_image")
    private String originImage;

    @Column(name = "edited_image")
    private String editedImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PostStatus status;

    @Column(name = "addr", nullable = false)
    private String addr;

    @Column(name = "detail_addr")
    private String detailAddr;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Column(name = "start_time", nullable = false)
    private String startTime;

    @Column(name = "end_time", nullable = false)
    private String endTime;

    @Column(name = "reward", nullable = false)
    private String reward;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> chatRooms;

    @Builder
    public Post(User author, String title, String content, String bugType, String bugName, String originImage, String editedImage, PostStatus status, String addr, String detailAddr, double latitude, double longitude, String startTime, String endTime, String reward) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.bugType = bugType;
        this.bugName = bugName;
        this.originImage = originImage;
        this.editedImage = editedImage;
        this.status = status;
        this.addr = addr;
        this.detailAddr = detailAddr;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reward = reward;
    }

    public static Post of(User author, SavePostRequest request, double latitude, double longitude) {
        return Post.builder()
                .author(author)
                .title(request.getTitle())
                .content(request.getContent())
                .bugType(request.getBug_type())
                .bugName(request.getBug_name())
                .addr(request.getAddress())
                .detailAddr(request.getAddress_datail())
                .startTime(request.getStart_time())
                .endTime(request.getEnd_time())
                .reward(request.getReward())
                .originImage(request.getOriginImageUrl())
                .editedImage(request.getEditedImageUrl())
                .status(PostStatus.DO)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    public void finish() {
        this.status = PostStatus.DONE;
    }

    public void cancel() {
        this.status = PostStatus.CANCEL;
    }

    public void updateTime(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}

