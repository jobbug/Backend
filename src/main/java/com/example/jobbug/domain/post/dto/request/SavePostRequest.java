package com.example.jobbug.domain.post.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class SavePostRequest {
    private String title;
    private String content;
    private String bug_type;
    private String address;
    private String address_datail;
    private String  start_time;
    private String end_time;
    private String reward;
    private String originImageUrl;
    private String editedImageUrl;
}