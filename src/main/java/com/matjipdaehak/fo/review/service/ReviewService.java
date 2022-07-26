package com.matjipdaehak.fo.review.service;

import com.matjipdaehak.fo.review.model.Review;
import java.util.*;

public interface ReviewService {

    /**
     * review객체를 전달받아 이를 DB에 새로운 row로 저장한다.<br/>
     * 이때 review_id는 auto increment로 생성되므로 review객체는 이것만 가지고 있지않으면 됨.
     * @param review - reviewId가 정해지지 않은 review 객체.
     */
    void createNewReview(Review review);


    /**
     * 날짜의 내림차순으로 리뷰를 가져온다.
     * @param placeId - place id에 해당하는 리뷰들을 리스트로 가져온다
     * @param scopeStart - 가져올 리뷰의 시작점
     * @param scopeEnd - 가져올 리뷰의 마지막 점
     * @return
     */
    List<Review> getReviewsByPlaceId(int placeId, int scopeStart, int scopeEnd);
}
