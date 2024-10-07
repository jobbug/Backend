package com.example.jobbug.domain.post.service;

import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.chat.repository.ChatRoomRepository;
import com.example.jobbug.domain.post.converter.PostConverter;
import com.example.jobbug.domain.post.dto.request.SavePostRequest;
import com.example.jobbug.domain.post.dto.request.UpdatePostRequest;
import com.example.jobbug.domain.post.dto.response.*;
import com.example.jobbug.domain.post.entity.Post;
import com.example.jobbug.domain.post.enums.PostStatus;
import com.example.jobbug.domain.post.repository.PostRepository;
import com.example.jobbug.domain.reservation.entity.ChatRoomStatus;
import com.example.jobbug.domain.review.repository.ReviewRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.security.core.parameters.P;
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
import java.util.stream.Collectors;

import static com.example.jobbug.domain.post.converter.PostConverter.toImageUploadResponse;
import static com.example.jobbug.global.exception.enums.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final ReviewRepository reviewRepository;
    @Value("${kakao.rest-api-key}")
    private String kakaoApiKey;

    // 서울역 위도, 경도
    private static final double defaultLatitude = 37.5559;
    private static final double defaultLongitude = 126.9723;

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final AIService aiService;
    private final S3Service s3Service;

    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
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

        return toImageUploadResponse(originImageUrl, aiImageUrl);
    }

    @Transactional
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

        return toImageUploadResponse(originImageUrl, editedImageUrl);
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

    @Transactional
    public MainPostInfoResponse getPosts(Long userId, String addr, int pageNum, String type, String sort) {
        Map<String, Double> userCoordinates = getUserCoordinates(userId, addr);
        double userLat = userCoordinates.get("latitude");
        double userLon = userCoordinates.get("longitude");

        Sort sortBy = Sort.by("createdAt");
        if ("최신순".equalsIgnoreCase(sort)) {
            sortBy = sortBy.descending();
        } else if ("오래된순".equalsIgnoreCase(sort)) {
            sortBy = sortBy.ascending();
        }

        Pageable pageable = PageRequest.of(pageNum, 5, sortBy);
        Page<Post> page = postRepository.findAllByStatus(PostStatus.DO, pageable);

        List<Post> filteredPosts = page.getContent().stream()
                .filter(post -> calculateDistance(userLat, userLon, post.getLatitude(), post.getLongitude()) <= 3)  // 3km 이내만 필터링
                .filter(post -> {
                    if (type.equalsIgnoreCase("전체")) {
                        return true;
                    } else {
                        return post.getBugType().equals(type);
                    }
                })
                .collect(Collectors.toList());

        return PostConverter.toMainPostInfoResponse(filteredPosts, pageable, filteredPosts.size());
    }

    @Transactional
    public PostDetailInfoResponse getPostDetail(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NotFoundException(NOT_FOUND_POST_EXCEPTION)
        );
        return PostConverter.toPostDetailInfoResponse(post);
    }

    @Transactional
    public void cancelPost(Long UserId, Long postId) {
        User user = userRepository.findById(UserId).orElseThrow(
                () -> new NotFoundException(NOT_FOUND_USER_EXCEPTION)
        );
        Post post = postRepository.findByIdAndAuthor(postId, user).orElseThrow(
                () -> new NotFoundException(NOT_FOUND_POST_EXCEPTION)
        );
        if(!post.getStatus().equals(PostStatus.DO)) {
            throw new BadRequestException(CANCEL_POST_EXCEPTION);
        }
        post.cancel();
    }

    @Transactional
    public void updatePost(Long userId, Long postId, UpdatePostRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(NOT_FOUND_USER_EXCEPTION)
        );
        Post post = postRepository.findByIdAndAuthor(postId, user).orElseThrow(
                () -> new NotFoundException(NOT_FOUND_POST_EXCEPTION)
        );
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByPostIdAndStatus(postId, ChatRoomStatus.MATCHED);
        if(!(post.getStatus().equals(PostStatus.DO)) || !(chatRooms.isEmpty())) {
            throw new BadRequestException(UPDATE_POST_EXCEPTION);
        }
        post.updateTime(request.getStartTime(), request.getEndTime());
    }

    @Transactional
    public GetUserRequestsResponse getUserRequests(Long userId) {
        User author = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(NOT_FOUND_USER_EXCEPTION)
        );
        List<Post> posts = postRepository.findAllByAuthor(author);
        return PostConverter.toGetUserRequestsResponse(posts, author, reviewRepository);
    }

    // Haversine 공식 기반 거리 계산 (단위: km)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }


    private Map<String, Double> getUserCoordinates(Long userId, String addr) {
        if(!addr.isEmpty()) {
            return fetchCoordinates(addr);
        }
        else {
            if (userId == null) {
                return getDefaultCoordinates();
            }

            User user = userRepository.findById(userId).orElseThrow(
                    () -> new NotFoundException(NOT_FOUND_USER_EXCEPTION)
            );
            return fetchCoordinates(user.getAddr());
        }
    }

    private Map<String, Double> getDefaultCoordinates() {
        Map<String, Double> coordinates = new HashMap<>();
        coordinates.put("latitude", defaultLatitude);
        coordinates.put("longitude", defaultLongitude);
        return coordinates;
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
