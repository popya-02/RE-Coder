package com.heartlink.review.model.dao;

import com.heartlink.review.model.dto.ReviewDto;
import com.heartlink.review.model.dto.ReviewPhotoDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewDao {
    private final JdbcTemplate jdbcTemplate;

    public ReviewDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 리뷰 저장
    public int saveReview(ReviewDto review) {
        String sql = "INSERT INTO REVIEW_BOARD (REVIEW_NO, REVIEW_TITLE, REVIEW_CONTENT, REVIEWER_USER_ID, REVIEWED_USER_ID) VALUES (REVIEW_SEQ.NEXTVAL, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, review.getReviewTitle(), review.getReviewContent(), review.getReviewerUserId(), review.getReviewedUserId());
    }

    // 리뷰 번호 얻기
    public String getLastReviewNo() {
        String sql = "SELECT REVIEW_SEQ.CURRVAL FROM DUAL";
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    // 리뷰 사진 저장
    public int saveReviewPhoto(ReviewPhotoDto reviewPhoto) {
        String sql = "INSERT INTO REVIEW_PHOTO_BOARD (PHOTO_NO, REVIEW_NO, PHOTO_PATH, PHOTO_NAME) VALUES (REVIEW_PHOTO_SEQ.NEXTVAL, ?, ?, ?)";
        return jdbcTemplate.update(sql, reviewPhoto.getReviewNo(), reviewPhoto.getPhotoPath(), reviewPhoto.getPhotoName());
    }
}


