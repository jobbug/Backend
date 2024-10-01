package com.example.jobbug.domain.post.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageUploadResponse {
    private String originImageURL;
    private String editedImageURL;
}
