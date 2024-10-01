package com.example.jobbug.domain.post.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SavePostResponse {
    private Long postId;
}
