package com.example.jobbug.domain.post.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MainPostInfoResponse {
    private int currentPage;
    private int totalNum;
    private int totalPage;
    private PostResponse[] posts;

    @Data
    @Builder
    public static class PostResponse {
        private Long postId;
        private String title;
        private String bug_type;
        private String bug_name;
        private String reward;
        private String addr;
        private String detail_addr;
        private double latitude;
        private double longitude;
        private String startTime;
        private String endTime;
        private LocalDateTime createdAt;
    }
}
