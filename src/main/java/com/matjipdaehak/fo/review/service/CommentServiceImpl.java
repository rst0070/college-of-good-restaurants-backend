package com.matjipdaehak.fo.review.service;

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
}
