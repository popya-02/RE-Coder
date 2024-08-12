package com.heartlink.review.model.service;

import com.heartlink.review.model.dao.ReviewDao;
import com.heartlink.review.model.dto.ReviewDto;
import com.heartlink.review.model.dto.ReviewPhotoDto;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDao reviewDao;

    public ReviewServiceImpl(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    @Override
    public boolean savePhotoReview(ReviewDto review, ReviewPhotoDto reviewPhoto) {
        int reviewResult = reviewDao.savePhotoReview(review);
        if (reviewResult > 0) {
            if (reviewPhoto != null) {
                String reviewNo = reviewDao.getLastReviewNo();
                reviewPhoto.setReviewNo(reviewNo);
                int photoResult = reviewDao.saveReviewPhoto(reviewPhoto);
                return photoResult > 0;
            }
            return true;
        }
        return false;
    }

    @Override
    public ReviewDto getReviewDetail(int reviewNo) {
        // 조회수 증가
        reviewDao.increaseReviewViews(reviewNo);
        return reviewDao.getReviewDetail(reviewNo);
    }

    @Override
    public void increaseReviewViews(int reviewNo) {
        reviewDao.increaseReviewViews(reviewNo);
    }
}
