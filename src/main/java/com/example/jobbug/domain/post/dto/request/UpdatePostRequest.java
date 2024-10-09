package com.example.jobbug.domain.post.dto.request;

import lombok.Data;

@Data
public class UpdatePostRequest {
    private String startTime;
    private String endTime;
}
