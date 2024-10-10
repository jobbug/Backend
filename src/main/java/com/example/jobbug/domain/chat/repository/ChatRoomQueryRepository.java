package com.example.jobbug.domain.chat.repository;


import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.chat.entity.QChatRoom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRoomQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<ChatRoom> findAllByUserIdInAuthorIdOrParticipantId(Long userId) {
        var chatRoom = QChatRoom.chatRoom;
        return queryFactory
                .selectFrom(chatRoom)
                .where(chatRoom.author.id.eq(userId).or(chatRoom.participant.id.eq(userId)))
                .fetch();
    }

    public boolean existsByUserIdInAuthorIdOrParticipantIdAndPostId(Long userId, Long postId) {
        var chatRoom = QChatRoom.chatRoom;
        return queryFactory
                .select(chatRoom)
                .from(chatRoom)
                .where(
                        chatRoom.author.id.eq(userId)
                                .or(chatRoom.participant.id.eq(userId)),
                        chatRoom.post.id.eq(postId)
                )
                .fetch().stream().findAny().isPresent();
    }
}
