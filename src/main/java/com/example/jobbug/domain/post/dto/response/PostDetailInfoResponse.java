package com.example.jobbug.domain.post.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDetailInfoResponse {
    private Long postId;
    private String nickname;
    private String profileImageUrl;
    private String title;
    private String bug_type;
    private String bug_name;
    private String reward;
    private String addr;
    private String content;
    private String startTime;
    private String endTime;
    private String originImageUrl;
    private String editImageUrl;
    private String createdAt;
}
