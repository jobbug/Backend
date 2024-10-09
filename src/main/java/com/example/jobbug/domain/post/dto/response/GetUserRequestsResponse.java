package com.example.jobbug.domain.post.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class GetUserRequestsResponse {
    private final List<GetUserRequestResponse> requests;

    @Data
    @Builder
    public static class GetUserRequestResponse {
        private final Long postId;
        private final String nickname;
        private final String profileImageUrl;
        private final String title;
        private final String bug_type;
        private final String bug_name;
        private final String reward;
        private final String addr;
        private final String content;
        private final String startTime;
        private final String endTime;
        private final String originImageUrl;
        private final String editImageUrl;
        private final LocalDateTime createdAt;
        private final ReviewResponse review;

        @Data
        @Builder
        public static class ReviewResponse {
            private final Long reviewId;
            private final Long writerId;
            private final String writerNickname;
            private final String writerProfileUrl;
            private final boolean isArrive;
            private final int lateTime;
            private final char isSuccess;
            private final String content;
            private final int point;
        }
    }
}
