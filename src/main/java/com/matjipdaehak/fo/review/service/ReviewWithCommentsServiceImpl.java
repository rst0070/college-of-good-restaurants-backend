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
        List<Review> reviews = this.reviewService.getReviewsByPlaceId(placeId, scopeStart, scopeEnd);
        return this.getReviewWithCommentsFromReviewList(reviews);
    }

    @Override
    public List<ReviewWithComments> getReviewWithCommentsByUserId(String userId, int scopeStart, int scopeEnd) {
        List<Review> reviews = this.reviewService.getReviewByUserId(userId, scopeStart, scopeEnd);
        return this.getReviewWithCommentsFromReviewList(reviews);
    }

    /**
     * Review의 리스트로부터 ReviewWithComments 리스트를 만들어 반환한다.
     * @param reviewList - List of Review object
     * @return List of ReviewWithComments
     */
    private List<ReviewWithComments> getReviewWithCommentsFromReviewList(List<Review> reviewList){
        Iterator<Review> reviews = reviewList.iterator();
        LinkedList<ReviewWithComments> result = new LinkedList<ReviewWithComments>();
        while(reviews.hasNext()){
            Review review = reviews.next();
            List<Comment> comments = commentService.getCommentByReviewId(review.getReviewId());

            ReviewWithComments reviewWithComments = new ReviewWithComments(review, comments);
            result.add(reviewWithComments);
        }
        return result;
    }
}
