package com.example.jobbug.domain.main.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MainSummaryResponse {
    private FastReviews fastReviews;
    private ReviewForMe reviewForMe;
    private Doing doing;

    @Data
    @Builder
    public static class FastReviews {
        private int count;
        private List<SimpleReviews> reviews;

        @Data
        @Builder
        public static class SimpleReviews {
            private Long chatRoomId;
            private String nickname;
        }
    }

    @Data
    @Builder
    public static class ReviewForMe {
        private int count;
        private List<SimpleReviews> reviews;

        @Data
        @Builder
        public static class SimpleReviews {
            private Long reviewId;
            private String nickname;
        }
    }

    @Data
    @Builder
    public static class Doing {
        private int count;
    }
}
