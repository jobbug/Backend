package com.example.jobbug.domain.user.dto.response;

import com.example.jobbug.domain.badge.enums.BadgeType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetUserProfileResponse {
    private Long userId;
    private String nickname;
    private String addr;
    private int totalRequests;
    private int successfulAcceptances;
    private List<Badge> badges;

    @Data
    @Builder
    public static class Badge {
        private String name;
        private BadgeType type;
    }
}
