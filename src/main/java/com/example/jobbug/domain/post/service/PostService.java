package com.example.jobbug.domain.post.service;

import com.example.jobbug.domain.post.dto.request.SavePostRequest;
import com.example.jobbug.domain.post.dto.response.ImageUploadResponse;
import com.example.jobbug.domain.post.dto.response.SavePostResponse;
import com.example.jobbug.domain.post.entity.Post;
import com.example.jobbug.domain.post.repository.PostRepository;
import com.example.jobbug.domain.user.entity.User;
import com.example.jobbug.domain.user.repository.UserRepository;
import com.example.jobbug.global.exception.model.AIException;
import com.example.jobbug.global.exception.model.BadRequestException;
import com.example.jobbug.global.exception.model.NotFoundException;
import com.example.jobbug.global.exception.model.S3Exception;
import com.example.jobbug.global.s3.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.jobbug.global.exception.enums.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    @Value("${kakao.rest-api-key}")
    private String kakaoApiKey;

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AIService aiService;
    private final S3Service s3Service;

    private final RestTemplate restTemplate = new RestTemplate();

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

    @Transactional
    public SavePostResponse savePost(Long userId, SavePostRequest request) {
        User author = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(NOT_FOUND_USER_EXCEPTION)
        );
        Map<String, Double> coordinates = fetchCoordinates(request.getAddress());
        Post post = Post.of(author, request, coordinates.get("latitude"), coordinates.get("longitude"));
        postRepository.save(post);
        return SavePostResponse.builder().postId(post.getId()).build();
    }

    private Map<String, Double> fetchCoordinates(String address) {
        URI uri = buildKakaoApiUri(address);
        HttpEntity<String> requestEntity = buildKakaoApiRequestEntity();

        ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, Map.class);
        return extractCoordinatesFromResponse(response);
    }

    private URI buildKakaoApiUri(String address) {
        try {
            return new URI("https://dapi.kakao.com/v2/local/search/address.json?query=" + URLEncoder.encode(address, "UTF-8"));
        } catch (URISyntaxException | UnsupportedEncodingException e) {
            log.error("잘못된 주소 형식입니다: {}", e.getMessage());
            throw new BadRequestException(NOT_FOUND_ADDRESS_EXCEPTION);
        }
    }

    private HttpEntity<String> buildKakaoApiRequestEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        return new HttpEntity<>(headers);
    }

    private Map<String, Double> extractCoordinatesFromResponse(ResponseEntity<Map> response) {
        Map<String, Object> meta = (Map<String, Object>) response.getBody().get("meta");
        if (meta == null || (int) meta.get("total_count") == 0) {
            throw new BadRequestException(NOT_FOUND_ADDRESS_EXCEPTION);
        }

        Map<String, Object> document = ((List<Map<String, Object>>) response.getBody().get("documents")).get(0);
        double latitude = Double.parseDouble((String) document.get("y"));
        double longitude = Double.parseDouble((String) document.get("x"));

        Map<String, Double> coordinates = new HashMap<>();
        coordinates.put("latitude", latitude);
        coordinates.put("longitude", longitude);

        return coordinates;
    }
}
