package com.example.jobbug.domain.chat.repository;

import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.reservation.entity.ChatRoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllByPostIdAndStatus(Long postId, ChatRoomStatus chatRoomStatus);
}

