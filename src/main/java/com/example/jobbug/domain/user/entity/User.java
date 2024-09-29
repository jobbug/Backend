package com.example.jobbug.domain.user.entity;

import com.example.jobbug.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "providerId", nullable = false)
    private String providerId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "nickname", nullable = false, length = 100)
    private String nickname;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "phone", nullable = false, length = 11)
    private String phone;

    @Column(name = "addr", nullable = false, length = 255)
    private String addr;

    @Column(name = "detail_addr", nullable = true, length = 255)
    private String detail_addr;

    @Column(name = "profile", nullable = true)
    private String profile;

    private String role;


    @Builder
    public User(String providerId, String name, String nickname, String email, String phone, String addr, String detail_addr, String profile, String role) {
        this.providerId = providerId;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.addr = addr;
        this.detail_addr = detail_addr;
        this.profile = profile;
        this.role = role;
    }
}
