package com.matjipdaehak.fo.review.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matjipdaehak.fo.review.model.Review;
import com.matjipdaehak.fo.review.model.ReviewWithComments;
import com.matjipdaehak.fo.review.service.ReviewService;
import com.matjipdaehak.fo.review.service.ReviewWithCommentsService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ReviewService reviewService;
    private final ReviewWithCommentsService reviewWithCommentsService;
    private final ObjectMapper objectMapper;
    private final SimpleDateFormat dateFormat;

    @Autowired
    public ReviewController(ReviewService reviewService,
                            ReviewWithCommentsService reviewWithCommentsService,
                            ObjectMapper objectMapper,
                            ApplicationContext context){
        this.reviewService = reviewService;
        this.reviewWithCommentsService = reviewWithCommentsService;
        this.objectMapper = objectMapper;
        this.dateFormat = context.getBean("yyyy-mm-dd", SimpleDateFormat.class);
    }

    /**
     * review의 review id가 결정되지 않았는데 이를 어느 순간에 결정해야하나..?
     * @param review - json형태의 요청 내용을 ReviewDeserializer가 review 객체로 변환
     * {
     *     "place_id" : "1",
     *     "user_id" : "wonbinkim",
     *     "post_date":"2022-06-01",
     *     "post_text":"테스트 리뷰",
     *     "rating":"4",
     *     "image_urls":[]
     * }
     */
    @RequestMapping("/add-review")
    public Map<String, String> addReview(@RequestBody Review review) throws Exception{
        reviewService.createNewReview(review);
        return Map.of("message", "success");
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

    /**
     *
     * @param json
     * {
     *     "user_id":"wonbinkim",
     *     "scope_start":"1",
     *     "scope_end":"10"
     * }
     * @return
     */
    @RequestMapping("/get-review-by-user-id")
    public List<ReviewWithComments> getReviewByUserId(@RequestBody JsonNode json){
        String userId = json.get("user_id").asText();
        int scopeStart = json.get("scope_start").asInt();
        int scopeEnd = json.get("scope_end").asInt();
        return this.reviewWithCommentsService.getReviewWithCommentsByUserId(userId, scopeStart, scopeEnd);
    }
}
