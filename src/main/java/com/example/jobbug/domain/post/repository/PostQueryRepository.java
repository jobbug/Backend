package com.example.jobbug.domain.post.repository;

import com.example.jobbug.domain.chat.entity.QChatRoom;
import com.example.jobbug.domain.post.entity.Post;
import com.example.jobbug.domain.post.entity.QPost;
import com.example.jobbug.domain.post.enums.PostStatus;
import com.example.jobbug.domain.reservation.entity.ChatRoomStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<Post> findAllByAuthorAndStatusOrderByCreatedAtDesc(Long userId, PostStatus status, PageRequest pageRequest) {
        QPost post = QPost.post;
        List<Post> posts = queryFactory.selectFrom(post)
                .where(post.author.id.eq(userId), statusCondition(status))
                .orderBy(post.createdAt.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .distinct()
                .fetch();

        long total = queryFactory.selectFrom(post)
                .where(post.author.id.eq(userId), statusCondition(status))
                .distinct()
                .fetch()
                .size();

        return new PageImpl<>(posts, pageRequest, total);
    }

    private BooleanExpression statusCondition(PostStatus status) {
        return status != null ? QPost.post.status.eq(status) : null;
    }

    public Page<Post> findAllByChatRoomParticipantAcceptance(Long userId, PageRequest pageRequest) {
        QPost post = QPost.post;
        QChatRoom chatRoom = QChatRoom.chatRoom;

        List<Post> posts = queryFactory.selectFrom(post)
                .join(post.chatRooms, chatRoom)
                .where(chatRoom.participant.id.eq(userId), chatRoom.status.eq(ChatRoomStatus.MATCHED))
                .orderBy(post.createdAt.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .distinct()
                .fetch();

        long totalCount = queryFactory.selectFrom(post)
                .join(post.chatRooms, chatRoom)
                .where(chatRoom.participant.id.eq(userId), chatRoom.status.eq(ChatRoomStatus.MATCHED))
                .distinct()
                .fetch()
                .size();

        return new PageImpl<>(posts, pageRequest, totalCount);
    }


}
