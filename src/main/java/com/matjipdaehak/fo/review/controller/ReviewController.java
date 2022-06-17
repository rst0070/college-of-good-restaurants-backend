package com.matjipdaehak.fo.review.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
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
    private final ObjectMapper objectMapper;

    @Autowired
    public ReviewController(ReviewService reviewService,
                            ObjectMapper objectMapper){
        this.reviewService = reviewService;
        this.objectMapper = objectMapper;
    }

    /**
     * {
     *     "place-id" : "1",
     *     "user-id" : "wonbinkim",
     *     "post-date":"2022-06-01",
     *     "post-text":"테스트 리뷰",
     *     "rating":"4",
     *     "image-urls":[]
     * }
     * @param reqBody - json형태의 요청 내용
     */
    @RequestMapping("/add-review")
    public Map<String, String> addReview(@RequestBody String reqBody, HttpServletResponse res){
        try{
            logger.info(reqBody);

            ObjectReader arrayToListReader = this.objectMapper.readerFor(new TypeReference<List<String>>() {});
            JsonNode json = this.objectMapper.readTree(reqBody);
            Date postDate = new SimpleDateFormat("yyyy-mm-dd").parse(json.get("post-date").asText());


            Review review = new Review(
                    json.get("place-id").asInt(),
                    json.get("user-id").asText(),
                    postDate,
                    json.get("post-text").asText(),
                    json.get("rating").asInt(),
                    arrayToListReader.readValue(json.get("image-urls"))
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
