package com.matjipdaehak.fo.review.service;

import com.matjipdaehak.fo.exception.CustomException;
import com.matjipdaehak.fo.exception.ErrorCode;
import com.matjipdaehak.fo.review.model.Review;
import com.matjipdaehak.fo.review.repository.ReviewRepository;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional
    public void createNewReview(Review review) throws CustomException {
        try{
            reviewRepository.insertReview(review);
        }catch(DataAccessException de){
            //DB에 리뷰 등록중 예외 발생시 로그에 기록
            //http error 코드로 예외 throw
            logger.warn(de.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * return Review object for inputed review id
     *
     * @param reviewId
     * @return
     */
    @Override
    public Review getReviewByReviewId(long reviewId) {
        return this.reviewRepository.selectReview(reviewId);
    }


    @Override
    public List<Review> getReviewsByPlaceId(int placeId, int scopeStart, int scopeEnd) {
        return this.reviewRepository.selectReviewByPlaceId(placeId, scopeStart, scopeEnd);
    }

    @Override
    public List<Review> getReviewByUserId(String userId, int scopeStart, int scopeEnd) {
        return this.reviewRepository.selectReviewByUserId(userId, scopeStart, scopeEnd);
    }

    @Transactional
    @Override
    public void updateReview(Review review) {
        Review prevReview = this.getReviewByReviewId(review.getReviewId());
        //해당 리뷰를 작성한 사용자가 아니라면 예외처리
        if(!prevReview.getUserId().equals(review.getUserId())) throw new CustomException(ErrorCode.UNAUTHORIZED);

        this.reviewRepository.updateReview(review);
    }

    /**
     * 특정 사용자가 특정 리뷰를 삭제하는 상황.
     * 즉
     * 1. 리뷰가 존재하는지 확인
     * 2. 사용자가 해당 리뷰의 소유자가 맞는지 확인
     * 3. 리뷰 삭제
     * 의 순서로 로직이 작동한다.
     *
     * @param userId
     * @param reviewId
     */
    @Override
    public void deleteReview(String userId, long reviewId) {
        try{
            Review review = this.getReviewByReviewId(reviewId);
            //해당 리뷰를 작성한 사용자가 아니라면 예외처리
            if(!review.getUserId().equals(userId)) throw new CustomException(ErrorCode.UNAUTHORIZED);

            this.reviewRepository.deleteReview(reviewId);

        }catch(DataAccessException de){

        }

    }

}
