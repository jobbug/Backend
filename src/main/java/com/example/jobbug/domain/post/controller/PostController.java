package com.example.jobbug.domain.post.controller;

import com.example.jobbug.domain.post.service.PostService;
import com.example.jobbug.global.dto.ErrorResponse;
import com.example.jobbug.global.exception.model.AIException;
import com.example.jobbug.global.exception.model.S3Exception;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/upload/ai")
    public ResponseEntity<?> uploadAIImage(
            @RequestPart(value = "originImage") MultipartFile image,
            @RequestPart(value = "emoticon") int emoticonNum
    ) {
        try {
            return ResponseEntity.ok(postService.uploadAIImage(image, emoticonNum));
        } catch (AIException | S3Exception e) {
            return ErrorResponse.error(e.getErrorCode());
        }
    }

}
