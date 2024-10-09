package com.example.jobbug.domain.user.repository;

import com.example.jobbug.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    User findByProviderId(String providerId);

    boolean existsByNickname(String nickname);
}
