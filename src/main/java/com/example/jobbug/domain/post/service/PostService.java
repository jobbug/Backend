package com.example.jobbug.domain.post.service;

import com.example.jobbug.domain.post.dto.response.ImageUploadResponse;
import com.example.jobbug.global.exception.model.AIException;
import com.example.jobbug.global.exception.model.S3Exception;
import com.example.jobbug.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.example.jobbug.global.exception.enums.ErrorCode.AI_IMAGE_CREATE_EXCEPTION;
import static com.example.jobbug.global.exception.enums.ErrorCode.S3_UPLOAD_FAILED;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final AIService aiService;
    private final S3Service s3Service;

    public ImageUploadResponse uploadAIImage(MultipartFile image, int emoticonNum) {

        String originImageUrl;
        try {
            originImageUrl = s3Service.upload(image, "origin");
        } catch (IllegalArgumentException | IOException e) {
            log.error("S3 업로드 중 오류 발생: {}", e.getMessage());
            throw new S3Exception(S3_UPLOAD_FAILED);
        }

        String aiImageUrl;
        try {
            aiImageUrl = aiService.generateEditedImage(originImageUrl, emoticonNum);
        } catch (IllegalArgumentException | IOException e) {
            log.error("AI 이미지 생성 중 오류 발생: {}", e.getMessage());
            throw new AIException(AI_IMAGE_CREATE_EXCEPTION);
        }

        return ImageUploadResponse.builder()
                .originImageURL(originImageUrl)
                .editedImageURL(aiImageUrl)
                .build();

    }

    public ImageUploadResponse uploadEditedImage(MultipartFile originImage, MultipartFile editedImage) {
        String originImageUrl;
        try {
            originImageUrl = s3Service.upload(originImage, "origin");
        } catch (IllegalArgumentException | IOException e) {
            log.error("S3 업로드 중 오류 발생: {}", e.getMessage());
            throw new S3Exception(S3_UPLOAD_FAILED);
        }

        String editedImageUrl;
        try {
            editedImageUrl = s3Service.upload(editedImage, "edited");
        } catch (IllegalArgumentException | IOException e) {
            log.error("S3 업로드 중 오류 발생: {}", e.getMessage());
            throw new S3Exception(S3_UPLOAD_FAILED);
        }

        return ImageUploadResponse.builder()
                .originImageURL(originImageUrl)
                .editedImageURL(editedImageUrl)
                .build();
    }
}
