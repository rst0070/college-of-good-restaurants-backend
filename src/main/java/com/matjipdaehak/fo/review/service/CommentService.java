package com.matjipdaehak.fo.review.service;
import java.util.*;
import com.matjipdaehak.fo.review.model.Comment;

/**
 * comment service
 */
public interface CommentService {

    Comment getCommentByCommentId(long commentId);

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
     * Comment를 update한다.
     * 이때 repository에서 comment_text와 comment_date만 수정하도록 제한되어있다.
     * @param comment
     */
    void updateComment(Comment comment);

    /**
     * 특정 댓글을 삭제한다.
     * @param commentId
     */
    void deleteComment(long commentId);

    /**
     * 특정 리뷰에 대한 댓글을 모두 삭제한다.
     * @param reviewId
     */
    void deleteCommentByReviewId(long reviewId);
}
