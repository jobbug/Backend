package com.example.jobbug.domain.post.converter;

import com.example.jobbug.domain.post.dto.response.GetUserRequestsResponse;
import com.example.jobbug.domain.post.dto.response.ImageUploadResponse;
import com.example.jobbug.domain.post.dto.response.MainPostInfoResponse;
import com.example.jobbug.domain.post.dto.response.PostDetailInfoResponse;
import com.example.jobbug.domain.post.entity.Post;
import com.example.jobbug.domain.review.entity.Review;
import com.example.jobbug.domain.review.repository.ReviewRepository;
import com.example.jobbug.domain.user.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PostConverter {
    public static ImageUploadResponse toImageUploadResponse(String originImageURL, String editedImageURL) {
        return ImageUploadResponse.builder()
                .originImageURL(originImageURL)
                .editedImageURL(editedImageURL)
                .build();
        }

    public static MainPostInfoResponse toMainPostInfoResponse(List<Post> posts, Pageable pageable, int totalElements) {
        return MainPostInfoResponse.builder()
                .currentPage(pageable.getPageNumber() + 1)
                .totalPage((int) Math.ceil((double) totalElements / pageable.getPageSize()))
                .totalNum(totalElements)
                .posts(posts.stream().map(PostConverter::toPostResponse).toArray(MainPostInfoResponse.PostResponse[]::new))
                .build();
    }

    public static MainPostInfoResponse.PostResponse toPostResponse(Post post) {
        return MainPostInfoResponse.PostResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .bug_type(post.getBugType())
                .bug_name(post.getBugName())
                .reward(post.getReward())
                .addr(post.getAddr())
                .detail_addr(post.getDetailAddr())
                .latitude(post.getLatitude())
                .longitude(post.getLongitude())
                .startTime(post.getStartTime())
                .endTime(post.getEndTime())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static PostDetailInfoResponse toPostDetailInfoResponse(Post post) {
        return PostDetailInfoResponse.builder()
                .postId(post.getId())
                .nickname(post.getAuthor().getName())
                .profileImageUrl(post.getAuthor().getProfile())
                .title(post.getTitle())
                .bug_type(post.getBugType())
                .bug_name(post.getBugName())
                .reward(post.getReward())
                .addr(post.getAddr())
                .content(post.getContent())
                .startTime(post.getStartTime())
                .endTime(post.getEndTime())
                .originImageUrl(post.getOriginImage())
                .editImageUrl(post.getEditedImage())
                .createdAt(String.valueOf(post.getCreatedAt()))
                .build();
    }

    public static GetUserRequestsResponse toGetUserRequestsResponse(List<Post> posts, User author, ReviewRepository reviewRepository) {
        return GetUserRequestsResponse.builder()
                .requests(posts.stream().map(post -> {
                    Review review = reviewRepository.findByPostId(post.getId());
                    GetUserRequestsResponse.GetUserRequestResponse.ReviewResponse reviewResponse = null;
                    if (review != null) {
                        reviewResponse = GetUserRequestsResponse.GetUserRequestResponse.ReviewResponse.builder()
                                .reviewId(review.getId())
                                .writerId(review.getWriter().getId())
                                .writerNickname(review.getWriter().getNickname())
                                .writerProfileUrl(review.getWriter().getProfile())
                                .isArrive(review.isArrive())
                                .lateTime(review.getLateTime())
                                .isSuccess(review.getIsSuccess())
                                .content(review.getContent())
                                .point(review.getPoint())
                                .build();
                    }
                    return GetUserRequestsResponse.GetUserRequestResponse.builder()
                            .postId(post.getId())
                            .nickname(author.getName())
                            .profileImageUrl(author.getProfile())
                            .title(post.getTitle())
                            .bug_type(post.getBugType())
                            .bug_name(post.getBugName())
                            .reward(post.getReward())
                            .addr(post.getAddr())
                            .content(post.getContent())
                            .startTime(post.getStartTime())
                            .endTime(post.getEndTime())
                            .originImageUrl(post.getOriginImage())
                            .editImageUrl(post.getEditedImage())
                            .createdAt(post.getCreatedAt())
                            .review(reviewResponse)
                            .build();
                }).toList())
                .build();
    }


}
