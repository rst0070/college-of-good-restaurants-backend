package com.matjipdaehak.fo.review.repository;

import com.matjipdaehak.fo.review.model.Review;
import org.springframework.dao.DataAccessException;
import java.util.*;

public interface ReviewRepository {

    /**
     * PLACE_id와 USER_id로 리뷰정보를 가져와 Review model로 반환
     * @param placeId - pk1
     * @param userId - pk2
     * @return Review - 해당 리뷰정보
     * @throws DataAccessException
     */
    Review selectReview(int placeId, String userId) throws DataAccessException;

    /**
     * place id를 통해 해당하는 리뷰를 가져온다.
     * @param placeId
     * @return
     * @throws DataAccessException
     */
    List<Review> selectReviewByPlaceId(int placeId) throws DataAccessException;

    /**
     * review를 DB에 저장한다.(REVIEW와 REVIEW_IMAGE_LIST테이블에)
     * 오류 발생시 해당 pk를 갖고있는 review정보를 지우며 예외발생
     * @param review
     */
    void insertReview(Review review) throws DataAccessException;

    /**
     * place id와 user id에 해당되는 리뷰, 리뷰이미지들에 대한 참조를 지운다.
     * @param placeId - pk1
     * @param userId - pk2
     * @throws DataAccessException - 지우는 과정에서 예외 발생시. place id 혹은 user id의 문제.
     */
    void deleteReview(int placeId, String userId) throws DataAccessException;
}
