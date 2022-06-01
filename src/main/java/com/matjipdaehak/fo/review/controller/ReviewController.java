package com.matjipdaehak.fo.review.controller;

import com.matjipdaehak.fo.review.model.Review;
import com.matjipdaehak.fo.review.service.ReviewService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    /**
     * {
     *     "place-id" : "1",
     *     "user-id" : "wonbinkim",
     *     "post-date":"2022-06-01",
     *     "post-text":"테스트 리뷰",
     *     "rating":"4"
     * }
     * @param req
     */
    @RequestMapping("/add-review")
    public Map<String, String> addReview(@RequestBody Map<String, String> req, HttpServletResponse res){
        try{
            Date postDate = new SimpleDateFormat("yyyy-mm-dd").parse(req.get("post-date"));
            Review review = new Review(
                    Integer.parseInt(req.get("place-id")),
                    req.get("user-id"),
                    postDate,
                    req.get("post-text"),
                    Integer.parseInt(req.get("rating"))
                    );
            reviewService.createNewReview(review);
        }catch(Exception ex){
            logger.info(ex.getMessage());
            res.setStatus(500);
            return Map.of("message", ex.getMessage());
        }
        return null;
    }
}
