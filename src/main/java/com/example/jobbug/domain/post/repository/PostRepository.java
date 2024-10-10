package com.example.jobbug.domain.post.repository;

import com.example.jobbug.domain.post.entity.Post;
import com.example.jobbug.domain.post.enums.PostStatus;
import com.example.jobbug.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 특정 bug_type과 좌표 기준으로 게시글 찾기
    Page<Post> findByBugTypeAndLatitudeBetweenAndLongitudeBetween(
            String bugType, double latMin, double latMax, double lonMin, double lonMax, Pageable pageable
    );

    // bug_type 관계없이 좌표 기준으로 게시글 찾기
    Page<Post> findByLatitudeBetweenAndLongitudeBetween(
            double latMin, double latMax, double lonMin, double lonMax, Pageable pageable
    );
    Page<Post> findAllByStatus(PostStatus status, Pageable pageable);

    Optional<Post> findByIdAndAuthor(Long postId, User author);

    List<Post> findAllByAuthorOrderByCreatedAtDesc(User author);

    int countByAuthor(User author);

    int countByAuthorAndBugType(User author, String bugType);
}
