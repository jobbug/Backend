package com.example.jobbug.domain.user.entity;

import com.example.jobbug.domain.badge.entity.Badge;
import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.post.entity.Post;
import com.example.jobbug.domain.review.entity.Review;
import com.example.jobbug.domain.user.dto.request.UserRegisterRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "User")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Setter
    @Column(name = "phone", nullable = false)
    private String phone;

    @Setter
    @Column(name = "addr", nullable = false)
    private String addr;

    @Setter
    @Column(name = "detail_addr")
    private String detail_addr;

    @Setter
    @Column(name = "profile", nullable = false)
    private String profile;

    @Column(name = "role", nullable = false)
    private String role;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Badge> badges = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> createdChatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> participatedChatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Builder
    public User(String email, String providerId, String name, String nickname, String phone, String addr, String detail_addr, String profile, String role) {
        this.email = email;
        this.providerId = providerId;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.addr = addr;
        this.detail_addr = detail_addr;
        this.profile = profile;
        this.role = role;
    }

    public static User of(String providerId, String email, UserRegisterRequest request, String profileImageUrl) {
        return User.builder()
                .providerId(providerId)
                .email(email)
                .name(request.getName())
                .nickname(request.getNickname())
                .phone(request.getPhone())
                .addr(request.getAddr())
                .detail_addr(request.getDetailAddr())
                .profile(profileImageUrl)
                .role("ROLE_USER")
                .build();
    }
}
