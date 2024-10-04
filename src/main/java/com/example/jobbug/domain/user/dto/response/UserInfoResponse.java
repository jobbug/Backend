package com.example.jobbug.domain.user.dto.response;

import com.example.jobbug.domain.user.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResponse {
    private Long userId;
    private String nickname;
    private String email;
    private String profileImageUrl;
}
