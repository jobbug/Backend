package com.example.jobbug.domain.post.controller;

import com.example.jobbug.domain.post.dto.request.SavePostRequest;
import com.example.jobbug.domain.post.service.PostService;
import com.example.jobbug.global.config.web.UserId;
import com.example.jobbug.global.dto.ErrorResponse;
import com.example.jobbug.global.exception.model.AIException;
import com.example.jobbug.global.exception.model.BadRequestException;
import com.example.jobbug.global.exception.model.NotFoundException;
import com.example.jobbug.global.exception.model.S3Exception;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("upload/edited")
    public ResponseEntity<?> uploadEditedImage(
            @RequestPart(value = "originImage") MultipartFile originImage,
            @RequestPart(value = "editedImage") MultipartFile editedImage
    ) {
        try {
            return ResponseEntity.ok(postService.uploadEditedImage(originImage, editedImage));
        } catch (S3Exception e) {
            return ErrorResponse.error(e.getErrorCode());
        }
    }

    @PostMapping("/post")
    public ResponseEntity<?> savePost(
            @UserId Long userId,
            @RequestBody SavePostRequest request
            ) {
        try {
            return ResponseEntity.ok(postService.savePost(userId, request));
        } catch (S3Exception | BadRequestException | NotFoundException e) {
            return ErrorResponse.error(e.getErrorCode());
        }
    }

}
