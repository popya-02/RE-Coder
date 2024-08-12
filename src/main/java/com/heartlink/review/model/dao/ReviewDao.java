package com.heartlink.review.model.dao;

import com.heartlink.review.model.dto.ReviewDto;
import com.heartlink.review.model.dto.ReviewPhotoDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewDao {
    private final SqlSession sqlSession;

    public ReviewDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    // 리뷰 저장
    public int saveReview(ReviewDto review) {
        return sqlSession.insert("reviewMapper.saveReview", review);
    }

    // 리뷰 번호 얻기
    public String getLastReviewNo() {
        return sqlSession.selectOne("reviewMapper.getLastReviewNo");
    }

    // 리뷰 사진 저장
    public int saveReviewPhoto(ReviewPhotoDto reviewPhoto) {
        return sqlSession.insert("reviewMapper.saveReviewPhoto", reviewPhoto);
    }
}
