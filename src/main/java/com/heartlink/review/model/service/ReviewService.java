package com.heartlink.review.model.service;

import com.heartlink.review.model.dto.ReviewDto;
import com.heartlink.review.model.dto.ReviewPhotoDto;

public interface ReviewService {
    boolean savePhotoReview(ReviewDto review, ReviewPhotoDto reviewPhoto);

    ReviewDto getReviewDetail(int reviewNo);

    void increaseReviewViews(int reviewNo);  // 조회수 증가 메서드 추가
}
