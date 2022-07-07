package com.matjipdaehak.fo.review.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matjipdaehak.fo.review.model.Review;
import com.matjipdaehak.fo.review.service.ReviewService;
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
    private final ObjectMapper objectMapper;

    @Autowired
    public ReviewController(ReviewService reviewService,
                            ObjectMapper objectMapper){
        this.reviewService = reviewService;
        this.objectMapper = objectMapper;
    }

    /**
     * {
     *     "place_id" : "1",
     *     "user_id" : "wonbinkim",
     *     "post_date":"2022-06-01",
     *     "post_text":"테스트 리뷰",
     *     "rating":"4",
     *     "image_urls":[]
     * }
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
     * @param json - json 형태의 요청
     * 요청 형태:
     *       {"place_id" : "1"}
     * @return Map
     * 응답형태:
     *        {
     *            "pages" : "5"
     *        }
     */
    @GetMapping("/get-pages")
    public Map<String, Integer> getNumOfPagesOfReview(@RequestBody JsonNode json){
        int placeId = json.get("place_id").asInt();
        return Map.of("pages", reviewService.numberOfPagesOfReviewOfPlace(placeId));
    }

    /**
     * 요청
     * {
     *     "place_id":"12313"
     *     "page":"1"
     * }
     *
     * 응답
     * [
     *  {}
     * ]
     * @param json
     * @return
     */
    @RequestMapping("/get-reviews")
    public List<Review> getReviews(@RequestBody JsonNode json){
        int placeId = json.get("place_id").asInt();
        int page = json.get("page").asInt();
        return reviewService.getReviewsByPlaceId(placeId, page);
    }
}
