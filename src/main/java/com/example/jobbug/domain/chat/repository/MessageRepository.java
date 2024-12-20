package com.example.jobbug.domain.chat.repository;

import com.example.jobbug.domain.chat.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findAllByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId, Pageable pageable);
}

