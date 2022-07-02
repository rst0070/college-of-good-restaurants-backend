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
     * 특정 PLACE에 속한 리뷰의 페이지수
     * 한 페이지에 10개의 리뷰가 들어간다.(네이버 지도가 10개)
     * @param placeId
     * @return
     */
    int numberOfPagesOfReviewOfPlace(int placeId);

    /**
     * @param placeId - place id에 해당하는 리뷰들을 리스트로 가져온다
     * @param page - 그 중 해당하는 page
     * @return
     */
    List<Review> getReviewsByPlaceId(int placeId, int page);
}
