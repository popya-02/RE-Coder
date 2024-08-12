package com.heartlink.review.model.service;

import com.heartlink.review.model.dto.ReviewDto;
import com.heartlink.review.model.dto.ReviewPhotoDto;

public interface ReviewService {


    public boolean saveReview(ReviewDto review, ReviewPhotoDto reviewPhoto);

    ReviewDto getReviewDetail(int reviewNo);

}
