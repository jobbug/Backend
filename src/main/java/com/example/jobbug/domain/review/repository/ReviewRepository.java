package com.example.jobbug.domain.review.repository;

import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.review.entity.Review;
import com.example.jobbug.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRoomId(ChatRoom chatRoom);
    List<Review> findByAuthor(User user);
}

