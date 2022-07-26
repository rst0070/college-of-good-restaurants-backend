package com.matjipdaehak.fo.review.service;

import com.matjipdaehak.fo.review.model.Comment;
import com.matjipdaehak.fo.review.model.Review;
import com.matjipdaehak.fo.review.model.ReviewWithComments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReviewWithCommentsServiceImpl implements ReviewWithCommentsService{

    private final ReviewService reviewService;
    private final CommentService commentService;

    @Autowired
    public ReviewWithCommentsServiceImpl(
            ReviewService reviewService,
            CommentService commentService){
        this.reviewService = reviewService;
        this.commentService = commentService;
    }

    @Override
    public List<ReviewWithComments> getReviewWithCommentsByPlaceId(int placeId, int scopeStart, int scopeEnd) {
        LinkedList<ReviewWithComments> result = new LinkedList<ReviewWithComments>();
        Iterator<Review> reviews = this.reviewService.getReviewsByPlaceId(placeId, scopeStart, scopeEnd).iterator();

        while(reviews.hasNext()){
            Review review = reviews.next();
            List<Comment> comments = commentService.getCommentByReviewId(review.getReviewId());

            ReviewWithComments reviewWithComments = new ReviewWithComments(review, comments);
            result.add(reviewWithComments);
        }
        return result;
    }
}
