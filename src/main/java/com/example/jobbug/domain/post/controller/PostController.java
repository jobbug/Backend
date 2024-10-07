package com.example.jobbug.domain.post.controller;

import com.example.jobbug.domain.post.dto.request.SavePostRequest;
import com.example.jobbug.domain.post.dto.request.UpdatePostRequest;
import com.example.jobbug.domain.post.service.PostService;
import com.example.jobbug.global.config.web.UserId;
import com.example.jobbug.global.dto.ErrorResponse;
import com.example.jobbug.global.dto.SuccessNonDataResponse;
import com.example.jobbug.global.dto.SuccessResponse;
import com.example.jobbug.global.exception.enums.SuccessCode;
import com.example.jobbug.global.exception.model.AIException;
import com.example.jobbug.global.exception.model.BadRequestException;
import com.example.jobbug.global.exception.model.NotFoundException;
import com.example.jobbug.global.exception.model.S3Exception;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.jobbug.global.exception.enums.SuccessCode.GET_USER_REQUESTS_SUCCESS;

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
            return SuccessResponse.success(SuccessCode.UPLOAD_AI_IMAGE_SUCCESS, postService.uploadAIImage(image, emoticonNum));
        } catch (AIException | S3Exception e) {
            return ErrorResponse.error(e.getErrorCode());
        }
    }

    @PostMapping("/upload/edited")
    public ResponseEntity<?> uploadEditedImage(
            @RequestPart(value = "originImage") MultipartFile originImage,
            @RequestPart(value = "editedImage") MultipartFile editedImage
    ) {
        try {
            return SuccessResponse.success(SuccessCode.UPLOAD_EDITED_IMAGE_SUCCESS, postService.uploadEditedImage(originImage, editedImage));
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
            return SuccessResponse.success(SuccessCode.CREATE_POST_SUCCESS ,postService.savePost(userId, request));
        } catch (S3Exception | BadRequestException | NotFoundException e) {
            return ErrorResponse.error(e.getErrorCode());
        }
    }

    @GetMapping("/post")
    public ResponseEntity<?> getPosts(
            @UserId Long userId,
            @RequestParam(required = false, defaultValue = "") String addr,
            @RequestParam(required = false, defaultValue = "0") int pageNum,
            @RequestParam(required = false, defaultValue = "전체") String type,
            @RequestParam(required = false, defaultValue = "최신순") String sort
    ) {
        try {
            return SuccessResponse.success(SuccessCode.GET_MAIN_POST_SUCCESS, postService.getPosts(userId, addr, pageNum, type, sort));
        } catch (BadRequestException e) {
            return ErrorResponse.error(e.getErrorCode());
        }
    }

    @GetMapping("/post/public")
    public ResponseEntity<?> getPosts(
            @RequestParam(required = false, defaultValue = "") String addr,
            @RequestParam(required = false, defaultValue = "0") int pageNum,
            @RequestParam(required = false, defaultValue = "전체") String type,
            @RequestParam(required = false, defaultValue = "최신순") String sort
    ) {
        try {
            return SuccessResponse.success(SuccessCode.GET_MAIN_POST_SUCCESS, postService.getPosts(null, addr, pageNum, type, sort));
        } catch (BadRequestException | NotFoundException e) {
            return ErrorResponse.error(e.getErrorCode());
        }
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getPostDetail(
            @PathVariable Long postId
    ) {
        try {
            return SuccessResponse.success(SuccessCode.GET_POST_DETAIL_SUCCESS, postService.getPostDetail(postId));
        } catch (NotFoundException e) {
            return ErrorResponse.error(e.getErrorCode());
        }
    }

    @PatchMapping("/post/{postId}/cancel")
    public ResponseEntity<?> cancelPost(
            @UserId Long userId,
            @PathVariable Long postId
    ) {
        try {
            postService.cancelPost(userId, postId);
            return SuccessNonDataResponse.success(SuccessCode.CANCEL_POST_SUCCESS);
        } catch (NotFoundException | BadRequestException e) {
            return ErrorResponse.error(e.getErrorCode());
        }
    }

    @PatchMapping("/post/{postId}/edit")
    public ResponseEntity<?> editPost(
            @UserId Long userId,
            @PathVariable Long postId,
            @RequestBody UpdatePostRequest request
    ) {
        try {
            postService.updatePost(userId, postId, request);
            return SuccessNonDataResponse.success(SuccessCode.UPDATE_POST_SUCCESS);
        } catch (NotFoundException | BadRequestException e) {
            return ErrorResponse.error(e.getErrorCode());
        }
    }

    @GetMapping("/post/user/{userId}/requests")
    public ResponseEntity<?> getUserRequests(
            @PathVariable Long userId
    ) {
        try {
            return SuccessResponse.success(GET_USER_REQUESTS_SUCCESS, postService.getUserRequests(userId));
        } catch (NotFoundException e) {
            return ErrorResponse.error(e.getErrorCode());
        }
    }

}
