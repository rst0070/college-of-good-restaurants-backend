package com.matjipdaehak.fo.review.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.matjipdaehak.fo.common.service.CommonService;
import com.matjipdaehak.fo.exception.CustomException;
import com.matjipdaehak.fo.exception.ErrorCode;
import com.matjipdaehak.fo.review.model.ExtendedReview;
import com.matjipdaehak.fo.review.model.Review;
import com.matjipdaehak.fo.review.model.ReviewWithComments;
import com.matjipdaehak.fo.review.service.ExtendedReviewService;
import com.matjipdaehak.fo.review.service.ReviewService;
import com.matjipdaehak.fo.review.service.ReviewWithCommentsService;
import com.matjipdaehak.fo.security.model.JwtInfo;
import com.matjipdaehak.fo.security.service.JwtService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ReviewService reviewService;
    private final ReviewWithCommentsService reviewWithCommentsService;
    private final ExtendedReviewService extendedReviewService;
    private final ObjectMapper objectMapper;
    private final ObjectReader stringListReader;
    private final CommonService commonService;
    private final JwtService jwtService;

    @Autowired
    public ReviewController(ReviewService reviewService,
                            ReviewWithCommentsService reviewWithCommentsService,
                            ExtendedReviewService extendedReviewService,
                            ObjectMapper objectMapper,
                            CommonService commonService,
                            JwtService jwtService){
        this.reviewService = reviewService;
        this.reviewWithCommentsService = reviewWithCommentsService;
        this.extendedReviewService = extendedReviewService;
        this.objectMapper = objectMapper;
        this.stringListReader = objectMapper.readerFor(new TypeReference<List<String>>() {});
        this.commonService = commonService;
        this.jwtService = jwtService;
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
                imageUrls = stringListReader.readValue(json.get("image_urls"));
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
    public List<ExtendedReview> getReviewByUserId(@RequestBody JsonNode json){
        String userId = json.get("user_id").asText();
        int scopeStart = json.get("scope_start").asInt();
        int scopeEnd = json.get("scope_end").asInt();
        return this.extendedReviewService.getExtendedReviewByUserId(userId, scopeStart, scopeEnd);
    }

    /**
     * 리뷰를 수정한다.
     * 규칙 (오직 자신의 리뷰만 수정가능하다.) --> 기존의 리뷰가 jwt사용자와 일치하는지 비교
     * 이때 post_date는 수정 요청 시점으로 변경된다.
     * 또한 place id와 user id는 수정불가하다.
     * @param req
     * @param json
     * {
     *     "review_id": "123213213",
     *     "post_text":"테스트 리뷰",
     *     "rating":"4",
     *     "image_urls":[]
     * }
     */
    @RequestMapping("/update-review")
    public void updateReview(HttpServletRequest req, @RequestBody JsonNode json){
        try{
            //jwt information inside of header
            final String jwt = req.getHeader("Authorization").substring(7);
            JwtInfo jwtInfo = this.jwtService.getJwtInfoFromJwt(jwt);

            //get review from DB and update data
            Review review = this.reviewService.getReviewByReviewId( json.get("review_id").asLong() );

            //compare userid in jwt vs user id in review for update
            //user could only change oneself review
            if( !jwtInfo.getUserId().equals( review.getUserId() ) ) throw new CustomException(ErrorCode.UNAUTHORIZED);

            //update data get from request
            review.setPostDate(new Date( this.commonService.getCurrentDate() ) );
            review.setPostText( json.get("post_text").asText() );
            review.setRating( json.get("rating").asInt() );

            try{//it's ok to have no review image
                review.setImageUrls( stringListReader.readValue(json.get("image_urls")) );
            }catch(IOException ie){}
            catch (NullPointerException ne){}
            catch (IllegalArgumentException ie){}

            // update data in db
            this.reviewService.updateReview(review);

        }catch(NullPointerException ne){
            throw new CustomException(ErrorCode.LACK_OF_DATA);
        }

    }

    /**
     * 특정 리뷰를 삭제한다. 또 리뷰의 댓글을 모두 삭제한다.
     * 이때 헤더의 jwt를 확인해 본인이 맞는지 확인한다.
     * 또한 해당 리뷰가 존재해야한다.
     *
     * @param json
     * {
     *     "review_id": "123123123"
     * }
     */
    @RequestMapping("/delete-review")
    public void deleteReview(HttpServletRequest req,@RequestBody JsonNode json){
        //jwt information inside of header
        final String jwt = req.getHeader("Authorization").substring(7);
        JwtInfo jwtInfo = this.jwtService.getJwtInfoFromJwt(jwt);

        //check review info about user who made the review
        Review review = this.reviewService.getReviewByReviewId(json.get("review_id").asLong());
        if(!review.getUserId().equals( jwtInfo.getUserId() )) throw new CustomException(ErrorCode.UNAUTHORIZED);
        //delete logic
        this.reviewService.deleteReview( review.getReviewId() );
    }
}
