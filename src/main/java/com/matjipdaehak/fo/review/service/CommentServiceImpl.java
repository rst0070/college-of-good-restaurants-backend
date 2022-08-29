package com.matjipdaehak.fo.review.service;

import com.matjipdaehak.fo.exception.CustomException;
import com.matjipdaehak.fo.exception.ErrorCode;
import com.matjipdaehak.fo.review.model.Comment;
import com.matjipdaehak.fo.review.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{


    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment getCommentByCommentId(long commentId) {
        return this.commentRepository.selectComment(commentId);
    }

    @Override
    public List<Comment> getCommentByReviewId(long reviewId) {
        return commentRepository.selectCommentByReviewId(reviewId);
    }

    @Override
    public void createNewComment(Comment comment) {
        this.commentRepository.insertComment(comment);
    }

    /**
     * @param comment
     */
    @Override
    public void updateComment(Comment comment) {
        this.commentRepository.updateComment(comment);
    }

    /**
     * 특정 댓글을 삭제한다.
     * @param commentId
     */
    @Override
    public void deleteComment(long commentId) {
        this.commentRepository.deleteComment(commentId);
    }

    /**
     * 특정 리뷰에 대한 댓글을 모두 삭제한다.
     *
     * @param reviewId
     */
    @Override
    public void deleteCommentByReviewId(long reviewId) {
        this.commentRepository.deleteCommentByReviewId(reviewId);
    }
}
