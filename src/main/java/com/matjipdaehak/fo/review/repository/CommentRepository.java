package com.matjipdaehak.fo.review.repository;

import java.util.*;
import com.matjipdaehak.fo.review.model.Comment;

public interface CommentRepository {

    /**
     * 해당하는 comment id가 있는지 확인한다.
     * @param commentId - 확인하려는 id
     * @return true - DB에 존재함. false - 존재안함 = 사용가능
     */
    boolean isIdExist(long commentId);

    /**
     * 1. comment id생성 및 중복확인
     * 2. comment 저장
     * @param comment - 저장하려는 comment객체 이때 comment id는 필요없음
     */
    void insertComment(Comment comment);

    /**
     *
     * @param commentId
     * @return
     */
    Comment selectComment(long commentId);

    /**
     * 특정 리뷰에 대한 모든 comment를 가져온다.
     * @param reviewId - review를 특정할 review id
     * @return List<Comment>
     */
    List<Comment> selectCommentByReviewId(long reviewId);

    /**
     *
     * @param comment
     */
    void updateComment(Comment comment);

    /**
     * 특정 댓글을 삭제한다.
     * @param commentId
     */
    void deleteComment(long commentId);
}
