package com.matjipdaehak.fo.review.service;

import com.matjipdaehak.fo.review.model.ExtendedReview;

import java.util.*;

/**
 * ExtendedReview객체를 활용하기 위한 서비스.
 * 조회의 용도로 ExtendedReview가 사용되므로 이 서비스는 기존의 다른 서비스들을 이용해 조회기능을 구현한다.
 */
public interface ExtendedReviewService {

    /**
     *
     * @param userId
     * @param scopeStart
     * @param scopeEnd
     * @return
     */
    List<ExtendedReview> getExtendedReviewByUserId(String userId, int scopeStart, int scopeEnd);
}
