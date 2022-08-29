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
    private final CommentService commentService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             CommentService commentService){
        this.reviewRepository = reviewRepository;
        this.commentService = commentService;
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
        this.reviewRepository.updateReview(review);
    }

    /**
     * 특정 리뷰와 그 댓글을 모두 삭제한다.
     * @param reviewId
     */
    @Override
    public void deleteReview(long reviewId) {
        this.commentService.deleteCommentByReviewId(reviewId);
        this.reviewRepository.deleteReview(reviewId);
    }

}
