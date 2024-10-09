package com.example.jobbug.domain.user.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateUserRequest {
    private String name;
    private String nickname;
    private String phone;
    private String addr;
    private String detailAddr;
    private MultipartFile profile;
}
