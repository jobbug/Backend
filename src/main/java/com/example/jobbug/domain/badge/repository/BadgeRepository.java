package com.example.jobbug.domain.badge.repository;

import com.example.jobbug.domain.badge.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
}

