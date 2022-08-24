package com.matjipdaehak.fo.review.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.matjipdaehak.fo.common.service.CommonService;
import com.matjipdaehak.fo.exception.CustomException;
import com.matjipdaehak.fo.exception.ErrorCode;
import com.matjipdaehak.fo.review.model.Review;
import com.matjipdaehak.fo.review.model.ReviewWithComments;
import com.matjipdaehak.fo.review.service.ReviewService;
import com.matjipdaehak.fo.review.service.ReviewWithCommentsService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ReviewService reviewService;
    private final ReviewWithCommentsService reviewWithCommentsService;
    private final ObjectMapper objectMapper;
    private final ObjectReader objectReader;
    private final CommonService commonService;

    @Autowired
    public ReviewController(ReviewService reviewService,
                            ReviewWithCommentsService reviewWithCommentsService,
                            ObjectMapper objectMapper,
                            CommonService commonService){
        this.reviewService = reviewService;
        this.reviewWithCommentsService = reviewWithCommentsService;
        this.objectMapper = objectMapper;
        this.objectReader = objectMapper.readerFor(new TypeReference<List<String>>() {});
        this.commonService = commonService;
    }

    /**
     * 새로운 리뷰를 추가한다. 이때 image_urls가 빈 배열일 수 있으므로 예외처리를한다.
     * @param json - json형태의 요청 내용을 ReviewDeserializer가 review 객체로 변환
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
    public Map<String, String> addReview(@RequestBody JsonNode json) throws CustomException{
        try{
            // json 데이터 받기
            int placeId = json.get("place_id").asInt();
            String userId = json.get("user_id").asText();
            String postText = json.get("post_text").asText();
            int rating = json.get("rating").asInt();
            List<String> imageUrls = null;

            //image url은 없어도 되므로 null인 경우에 대한 예외처리
            try{
                imageUrls = objectReader.readValue(json.get("image_urls"));
            }catch(NullPointerException ne){ logger.info("null of image urls"); }
            catch(IllegalArgumentException ie){ logger.info("image urls를 찾을수 없음");}
            catch (IOException ie){ logger.info(ie.getMessage()); }

            //리뷰 객체 생성
            Review review = new Review();
            review.setPlaceId(placeId);
            review.setUserId(userId);
            review.setPostText(postText);
            review.setRating(rating);
            review.setPostDate( new Date(commonService.getCurrentDate()) );
            review.setImageUrls(imageUrls);

            //리뷰 저장
            reviewService.createNewReview(review);

        }catch(NullPointerException ex){
            throw new CustomException(ErrorCode.LACK_OF_DATA);
        }
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
