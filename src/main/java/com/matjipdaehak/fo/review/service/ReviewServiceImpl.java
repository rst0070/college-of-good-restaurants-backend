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



    @Override
    public List<Review> getReviewsByPlaceId(int placeId, int scopeStart, int scopeEnd) {
        return this.reviewRepository.selectReviewByPlaceId(placeId, scopeStart, scopeEnd);
    }

    @Override
    public List<Review> getReviewByUserId(String userId, int scopeStart, int scopeEnd) {
        return this.reviewRepository.selectReviewByUserId(userId, scopeStart, scopeEnd);
    }

}
