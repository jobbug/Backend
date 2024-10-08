package com.example.jobbug.domain.badge.repository;

import com.example.jobbug.domain.badge.entity.Badge;
import com.example.jobbug.domain.badge.enums.BadgeType;
import com.example.jobbug.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    void deleteByUserAndType(User user, BadgeType badgeType);

    void deleteByUserAndName(User user, String name);
}

