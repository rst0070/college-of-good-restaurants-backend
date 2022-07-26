package com.matjipdaehak.fo.review.service;
import java.util.*;
import com.matjipdaehak.fo.review.model.Comment;

/**
 * comment service
 */
public interface CommentService {

    /**
     * Review에 대한 comment들을 가져온다.
     * @return List<Comment>
     */
    List<Comment> getCommentByReviewId(long reviewId);
}
