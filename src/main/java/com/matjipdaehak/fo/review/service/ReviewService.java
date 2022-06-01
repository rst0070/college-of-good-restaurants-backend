package com.matjipdaehak.fo.review.service;

import com.matjipdaehak.fo.review.model.Review;

public interface ReviewService {

    /**
     * review객체를 전달받아 이를 DB에 새로운 row로 저장한다.
     * @param review
     */
    void createNewReview(Review review);
}
