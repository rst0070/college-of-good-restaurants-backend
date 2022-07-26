package com.matjipdaehak.fo.review.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matjipdaehak.fo.review.model.Review;
import com.matjipdaehak.fo.review.model.ReviewWithComments;
import com.matjipdaehak.fo.review.service.ReviewService;
import com.matjipdaehak.fo.review.service.ReviewWithCommentsService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ReviewService reviewService;
    private final ReviewWithCommentsService reviewWithCommentsService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ReviewController(ReviewService reviewService,
                            ReviewWithCommentsService reviewWithCommentsService,
                            ObjectMapper objectMapper){
        this.reviewService = reviewService;
        this.reviewWithCommentsService = reviewWithCommentsService;
        this.objectMapper = objectMapper;
    }

    /**
     *
     * {
     *     "place_id" : "1",
     *     "user_id" : "wonbinkim",
     *     "post_date":"2022-06-01",
     *     "post_text":"테스트 리뷰",
     *     "rating":"4",
     *     "image_urls":[]
     * }
     *
     * @param json - json형태의 요청 내용
     */
    @RequestMapping("/add-review")
    public Map<String, String> addReview(@RequestBody String json, HttpServletResponse res){
        try{
            Review review = objectMapper.readValue(json, Review.class);
            reviewService.createNewReview(review);
        }catch (JsonMappingException jex){
            logger.info(jex.getMessage());
        }catch(Exception ex){
            logger.info(ex.getMessage());
            res.setStatus(500);
            return Map.of("message", ex.getMessage());
        }
        return null;
    }


    /**
     * /review/get-reviews
     *
     * @param json
     * @return
     */
    @RequestMapping("/get-reviews")
    public List<ReviewWithComments> getReviews(@RequestBody JsonNode json){
        int placeId = json.get("place_id").asInt();
        int scopeStart = json.get("scope_start").asInt();
        int scopeEnd = json.get("scope_end").asInt();
        return reviewWithCommentsService.getReviewWithCommentsByPlaceId(placeId, scopeStart, scopeEnd);
    }
}
