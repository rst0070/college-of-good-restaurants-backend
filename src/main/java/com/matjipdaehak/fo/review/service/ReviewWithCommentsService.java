package com.matjipdaehak.fo.review.service;

import com.matjipdaehak.fo.review.model.ReviewWithComments;
import java.util.List;

/**
 * ReviewService와 CommentService를 사용하여 결과를 조합해 ReviewWithComments를 생성하는것이 주 역할.
 */
public interface ReviewWithCommentsService {

    /**
     * ReviewWithComments List를 반환한다.
     * 이때 특정 가게에 속하며 날짜의 내림차순으로 결과의 범위를 정한다.
     * @param placeId
     * @param scopeStart
     * @param scopeEnd
     * @return
     */
    List<ReviewWithComments> getReviewWithCommentsByPlaceId(int placeId, int scopeStart, int scopeEnd);
}
