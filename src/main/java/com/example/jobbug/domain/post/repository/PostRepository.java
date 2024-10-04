package com.example.jobbug.domain.post.repository;

import com.example.jobbug.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
    Page<Post> findAllByStatus(String status, Pageable pageable);
}
