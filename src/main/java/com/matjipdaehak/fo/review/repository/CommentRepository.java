package com.matjipdaehak.fo.review.repository;

import java.util.*;
import com.matjipdaehak.fo.review.model.Comment;

public interface CommentRepository {
    /**
     * 특정 리뷰에 대한 모든 comment를 가져온다.
     * @param reviewId - review를 특정할 review id
     * @return List<Comment>
     */
    List<Comment> selectCommentByReviewId(long reviewId);
}
