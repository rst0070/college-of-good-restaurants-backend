package com.matjipdaehak.fo.review.service;

import com.matjipdaehak.fo.place.service.ExtendedPlaceService;
import com.matjipdaehak.fo.review.model.ExtendedReview;
import com.matjipdaehak.fo.review.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
public class ExtendedReviewServiceImpl implements ExtendedReviewService{

    private final ExtendedPlaceService extendedPlaceService;
    private final ReviewService reviewService;
    private final CommentService commentService;

    @Autowired
    public ExtendedReviewServiceImpl(
            ExtendedPlaceService extendedPlaceService,
            ReviewService reviewService,
            CommentService commentService
    ){
        this.extendedPlaceService = extendedPlaceService;
        this.reviewService = reviewService;
        this.commentService = commentService;
    }


    /**
     * @param userId
     * @param scopeStart
     * @param scopeEnd
     * @return
     */
    @Override
    public List<ExtendedReview> getExtendedReviewByUserId(String userId, int scopeStart, int scopeEnd) {
        LinkedList<ExtendedReview> result = new LinkedList<ExtendedReview>();
        Iterator<Review> reviews = this.reviewService.getReviewByUserId(userId, scopeStart, scopeEnd).iterator();

        while(reviews.hasNext()){
            Review now = reviews.next();
            ExtendedReview er = new ExtendedReview();

            er.setReview( now );
            er.setPlace( this.extendedPlaceService.getExtendedPlaceByPlaceId(now.getPlaceId()) );
            er.setComments( commentService.getCommentByReviewId(now.getReviewId()) );

            result.add(er);
        }
        return result;
    }
}
