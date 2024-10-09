package com.example.jobbug.domain.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResponse {
    private Long userId;
    private String name;
    private String nickname;
    private String email;
    private String profileImageUrl;
    private String phone;
    private String addr;
    private String detailAddr;
}
