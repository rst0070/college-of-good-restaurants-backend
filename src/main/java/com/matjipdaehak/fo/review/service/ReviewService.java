package com.matjipdaehak.fo.review.service;

import com.matjipdaehak.fo.review.model.Review;
import java.util.*;

public interface ReviewService {

    /**
     * review객체를 전달받아 이를 DB에 새로운 row로 저장한다.
     * @param review
     */
    void createNewReview(Review review);

    /**
     * place id에 해당하는 리뷰들을 리스트로 가져온다
     * @param placeId
     * @return
     */
    List<Review> getReviewsByPlaceId(int placeId);
}
