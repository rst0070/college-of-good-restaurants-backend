package com.matjipdaehak.fo.review.repository;

import com.matjipdaehak.fo.review.model.Review;
import org.springframework.dao.DataAccessException;
import java.util.*;

public interface ReviewRepository {

    /**
     * review_id를 이용해 review 객체 생성
     * @param reviewId - REVIEW의 primary key
     * @return Review - 해당 리뷰정보
     * @throws DataAccessException
     */
    Review selectReview(long reviewId) throws DataAccessException;

    /**
     * place id를 통해 해당하는 리뷰를 가져온다.
     * @param placeId
     * @return
     * @throws DataAccessException
     */
    List<Review> selectReviewByPlaceId(int placeId, int scopeStart, int scopeEnd) throws DataAccessException;

    /**
     * 특정 user_id의 사용자가 작성한 리뷰를 가져온다.
     * @param userId
     * @param scopeStart
     * @param scopeEnd
     * @return
     * @throws DataAccessException
     */
    List<Review> selectReviewByUserId(String userId, int scopeStart, int scopeEnd) throws DataAccessException;

    /**
     * review를 DB에 저장한다.(REVIEW와 REVIEW_IMAGE_LIST테이블에)
     * 오류 발생시 해당 pk를 갖고있는 review정보를 지우며 예외발생
     * @param review
     */
    void insertReview(Review review) throws DataAccessException;

    /**
     * Designed to do not update place_id, review_id, user_id of review
     * Thus,...
     * @param review
     * @throws DataAccessException
     */
    void updateReview(Review review) throws DataAccessException;

    /**
     * review_id에 해당되는 리뷰, 리뷰이미지들에 대한 참조를 지운다.
     * @param reviewId - 지울 리뷰의 id
     * @throws DataAccessException - 지우는 과정에서 예외 발생시. review id의 문제.
     */
    void deleteReview(long reviewId) throws DataAccessException;
}
