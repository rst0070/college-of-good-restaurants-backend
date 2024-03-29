package com.matjipdaehak.fo.place.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.matjipdaehak.fo.exception.DataAlreadyExistException;
import com.matjipdaehak.fo.place.model.ExtendedPlace;
import com.matjipdaehak.fo.place.model.RecommendedPlace;
import com.matjipdaehak.fo.place.service.ExtendedPlaceService;
import org.slf4j.*;
import com.matjipdaehak.fo.place.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import com.matjipdaehak.fo.place.model.Place;

@RestController
@RequestMapping("/place")
public class PlaceController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PlaceService placeService;
    private final ExtendedPlaceService extendedPlaceService;

    @Autowired
    public PlaceController(
            PlaceService placeService,
            ExtendedPlaceService extendedPlaceService){
        this.placeService = placeService;
        this.extendedPlaceService = extendedPlaceService;
    }

    /**
     * 예시 데이터
     * {
     *  "user_id": "wonbinkim"
     * 	"category_name": "테마파크",
     * 	"kakao_id": "22225498",
     * 	"phone": "0507-1352-1401",
     * 	"place_name": "산토끼노래동산",
     * 	"place_url": "http://place.map.kakao.com/22225498",
     * 	"road_address_name": "경남 창녕군 이방면 이방로 623",
     * 	"x": "128.383585226179",
     * 	"y": "35.5777087822771",
     * 	"image-url":"https://asdadasd"
     * }
     * 예외를 세분화해 처리할 필요가 있음. ex. 서버 내부오류 or 입력데이터 오류
     * @param req
     * @param res
     */
    @RequestMapping("/add-place")
    public Map<String, String> addPlace(@RequestBody Map<String, String> req, HttpServletResponse res){
        int placeId = 0;
        try{
            Place place = new Place(
                    req.get("kakao_id"),
                    req.get("place_name"),
                    req.get("road_address_name"),
                    Double.parseDouble(req.get("y")),
                    Double.parseDouble(req.get("x")),
                    req.get("phone"),
                    req.get("category_name"),
                    req.get("image-url")
            );

            if( this.placeService.isPlaceExists(place.getAddress(), place.getName()) ){
                placeId = this.placeService.getPlaceId(place.getAddress(), place.getName());
                throw new DataAlreadyExistException("해당 가게가 이미 존재합니다.");
            }

            placeId = placeService.createNewPlace(place, req.get("user_id"));
        }catch(Exception ex){
            logger.info(ex.getMessage());
            res.setStatus(500);
            return Map.of("message", ex.getMessage(),
                    "place_id", ""+placeId);
        }
        return Map.of(
                "message", "success",
                "place_id", ""+placeId
        );
    }

    /**
     * 장소에 대해 키워드 서칭
     * 키워드와 id로 확인
     * @param {
     *     "keyword" : "고기",
     *     "college_id" : "123",
     *     "scope_start":"1",
     *     "scope_end":"10"
     * }
     *
     * @return [
     *     {
     *         "name": "맛나식당",
     *         "address": "서울 동대문구 망우로10길 44",
     *         "latitude": 37.5871346761732,
     *         "longitude": 127.056588125178,
     *         "phone": "010-9600-0657",
     *         "category": "한식",
     *         "rating": 4.0,
     *         "place_id": 1,
     *         "kakao_place_id": "195227114",
     *         "image_url": null,
     *         "review_count": 1,
     *         "like_count": 0
     *     }
     * ]
     */
    @RequestMapping("/search-place")
    public List<ExtendedPlace> searchPlace(@RequestBody JsonNode json){
        int collegeId = json.get("college_id").asInt();
        String keyword = json.get("keyword").asText();
        int scopeStart = json.get("scope_start").asInt();
        int scopeEnd = json.get("scope_end").asInt();
        return extendedPlaceService.keywordSearchExtendedPlace(collegeId, keyword, scopeStart, scopeEnd);
    }

    /**
     * @param {
     *     "place_id":"1"
     * }
     * @return {
     *     "name": "맛나식당",
     *     "address": "서울 동대문구 망우로10길 44",
     *     "latitude": 37.5871346761732,
     *     "longitude": 127.056588125178,
     *     "phone": "010-9600-0657",
     *     "category": "한식",
     *     "rating": 4.0,
     *     "place_id": 1,
     *     "kakao_place_id": "195227114",
     *     "image_url": null,
     *     "review_count": 1,
     *     "like_count": 0
     * }
     */
    @PostMapping("/get-place")
    public ExtendedPlace getPlace(@RequestBody JsonNode json){
        int placeId = json.get("place_id").asInt();
        return extendedPlaceService.getExtendedPlaceByPlaceId(placeId);
    }

    /**
     * 특정 사용자가 등록한 place를 ExtendedPlace list로 반환한다.
     * @param json
     * @return
     */
    @PostMapping("/get-place-by-user-id")
    public List<ExtendedPlace> getPlaceByUserId(@RequestBody JsonNode json){
        String userId = json.get("user_id").asText();
        int scopeStart = json.get("scope_start").asInt();
        int scopeEnd = json.get("scope_end").asInt();
        return extendedPlaceService.getExtendedPlaceByRegistrantId(userId, scopeStart, scopeEnd);
    }

    /**
     * RecommendedPlace형식으로 10개의 Place를 반환한다.
     * 이때 어떤 방식으로 추천한것인지 부연설명 추가됨.
     * @param json
     * {
     *     "college_id":31
     * }
     * @return
     */
    @PostMapping("/get-recommended-place")
    public RecommendedPlace getRecommendedPlace(@RequestBody JsonNode json){
        int collegeId = json.get("college_id").asInt();
        List<ExtendedPlace> places = this.extendedPlaceService.getTop10PlaceInCollegeByReviewCount(collegeId);
        return new RecommendedPlace(
                "리뷰순 추천",
                places
        );
    }
}
