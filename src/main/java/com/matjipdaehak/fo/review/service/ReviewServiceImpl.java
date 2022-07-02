package com.matjipdaehak.fo.review.service;

import com.matjipdaehak.fo.review.model.Review;
import com.matjipdaehak.fo.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional
    public void createNewReview(Review review) {
        reviewRepository.insertReview(review);
    }

    @Override
    public int numberOfPagesOfReviewOfPlace(int placeId) {
        int numberOfReview = reviewRepository.numberOfReviewOfPlace(placeId);
        int numberOfPages = numberOfReview/10;

        if(numberOfReview % 10 != 0) numberOfPages++;
        return numberOfPages;
    }

    @Override
    public List<Review> getReviewsByPlaceId(int placeId, int page) {
        return this.reviewRepository.selectReviewByPlaceId(placeId, page);
    }

}
