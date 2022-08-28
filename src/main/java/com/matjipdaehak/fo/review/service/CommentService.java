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

    /**
     * DB에 새로운 댓글객체를 등록한다.
     * @param comment - comment id가 없는 comment객체
     */
    void createNewComment(Comment comment);

    /**
     *
     * @param comment
     */
    void updateComment(Comment comment);

    /**
     * 특정 댓글을 삭제한다. 이때 삭제는 작성한 본인이 할 수 있으므로 jwt의 사용자 아이디를 넘겨주면됨
     * @param userId
     * @param commentId
     */
    void deleteComment(String userId, long commentId);
}
