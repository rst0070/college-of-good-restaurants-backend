package com.matjipdaehak.fo.review.service;

import com.matjipdaehak.fo.exception.CustomException;
import com.matjipdaehak.fo.review.model.Review;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public interface ReviewService {

    /**
     * review객체를 전달받아 이를 DB에 새로운 row로 저장한다.<br/>
     * 이때 review_id는 auto increment로 생성되므로 review객체는 이것만 가지고 있지않으면 됨.
     * @param review - reviewId가 정해지지 않은 review 객체.
     */
    @Transactional
    void createNewReview(Review review) throws CustomException;

    /**
     * return Review object for inputed review id
     * @param reviewId
     * @return
     */
    Review getReviewByReviewId(long reviewId);

    /**
     * 날짜의 내림차순으로 리뷰를 가져온다.
     * @param placeId - place id에 해당하는 리뷰들을 리스트로 가져온다
     * @param scopeStart - 가져올 리뷰의 시작점
     * @param scopeEnd - 가져올 리뷰의 마지막 점
     * @return
     */
    List<Review> getReviewsByPlaceId(int placeId, int scopeStart, int scopeEnd);

    List<Review> getReviewByUserId(String userId, int scopeStart, int scopeEnd);

    /**
     * 특정 사용자가 리뷰를 수정하는 상황이다.
     * 즉 해당 메소드는
     * 1. 해당 하는 아이디의 리뷰가 존재하는지 확인
     * 2. 해당 리뷰의 등록자가 기존의 리뷰 등록자와 같은지 확인.
     * 3. 기존 리뷰 삭제
     * 4. 새로운 리뷰 등록
     * @param review
     */
    void updateReview(Review review);

    /**
     * 특정 리뷰와 그 댓글을 모두 삭제한다.
     * 이때 댓글이 리뷰를 참조하므로 댓글을 먼저 삭제해야함
     * @param reviewId
     */
    void deleteReview(long reviewId);
}
