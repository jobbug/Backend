package com.example.jobbug.domain.badge.entity;

import com.example.jobbug.domain.badge.enums.BadgeType;
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
public class Badge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private BadgeType type;

    @Builder
    public Badge(User user, String name, BadgeType type) {
        this.user = user;
        this.name = name;
        this.type = type;
    }
}

