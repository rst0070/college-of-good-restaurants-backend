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
    public List<Comment> getCommentByReviewId(long reviewId) {
        return commentRepository.selectCommentByReviewId(reviewId);
    }

    @Override
    public void createNewComment(Comment comment) {
        this.commentRepository.insertComment(comment);
    }

    /**
     * 파라미터 comment객체의 user id를 검사해 이미 저장되어있는 user id와 동일한지 확인.
     * 아닐경우 예외발생
     * @param comment
     */
    @Override
    public void updateComment(Comment comment) {
        Comment prevComment = this.commentRepository.selectComment(comment.getCommentId());
        if( !comment.getUserId().equals(prevComment.getUserId()) ) throw new CustomException(ErrorCode.UNAUTHORIZED);

        this.commentRepository.updateComment(comment);
    }

    /**
     * 특정 댓글을 삭제한다. 이때 삭제는 작성한 본인이 할 수 있으므로 jwt의 사용자 아이디를 넘겨주면됨
     *
     * @param userId
     * @param commentId
     */
    @Override
    public void deleteComment(String userId, long commentId) {
        Comment comment = this.commentRepository.selectComment(commentId);
        if( !userId.equals( comment.getUserId() ) ) throw new CustomException(ErrorCode.UNAUTHORIZED);

        this.commentRepository.deleteComment(commentId);
    }
}
