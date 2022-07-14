package com.matjipdaehak.fo.like.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.matjipdaehak.fo.like.model.ExtendedPlaceLike;
import com.matjipdaehak.fo.like.model.PlaceLike;
import com.matjipdaehak.fo.like.service.PlaceLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * place에 대한 좋아요 기능은 어떤 동작을 해야하는가?
 * 1. 좋아요를 등록한다. -> 가게, 유저, 날짜를 특정할 수 있어야한다.
 * 2. 좋아요를 취소한다. -> 1번과 마찬가지의 정보가 필요하다.
 * 3. 사용자의 좋아요 목록을 가져온다. -> 유저를 특정할 수 있어야한다.
 * 4. 가게의 좋아요수를 카운트한다. -> 가게를 특정해야함
 *
 */
@RestController
@RequestMapping("/place-like")
public class PlaceLikeController {

    private final PlaceLikeService placeLikeService;
    private final SimpleDateFormat dateFormat;

    @Autowired
    public PlaceLikeController(
            PlaceLikeService placeLikeService
    ) {
        this.placeLikeService = placeLikeService;
        this.dateFormat = new SimpleDateFormat("yyyy-mm-dd");
    }

    /**
     * 유저가 place like를 했는지 확인한다.
     * @param json - {"place_id" : "1", "user_id" : "wonbinkim"}
     * @return {"result":"true"} || {"result":"false"}
     */
    @PostMapping("/exist")
    public Map<String, String> checkLike(@RequestBody JsonNode json){
        int placeId = json.get("place_id").asInt();
        String userId = json.get("user_id").asText();
        boolean exist = placeLikeService.checkLikeExist(placeId, userId);
        if(exist)
            return Map.of("result", "true");
        return Map.of("result", "false");
    }

    /**
     * 좋아요 등록
     * @param json - {"place_id":"11", "user_id":"dd", "like_date":"2022-07-03"} 이와 같은 형식의 데이터
     */
    @PostMapping("/add")
    public void addLike(@RequestBody JsonNode json) throws Exception{
        try{
            int placeId = json.get("place_id").asInt();
            String userId = json.get("user_id").asText();
            Date likeDate = dateFormat.parse(json.get("like_date").asText());
            placeLikeService.addLike(placeId, userId, likeDate);
        }catch(ParseException pe){
            throw new Exception("dddd");
        }
    }

    /**
     * 좋아요 취소
     * @param json - {"place_id":"11", "user_id":"dd"} 이와 같은 형식의 데이터
     */
    @PostMapping("/remove")
    public void removeLike(@RequestBody JsonNode json){
        int placeId = json.get("place_id").asInt();
        String userId = json.get("user_id").asText();
        placeLikeService.removeLike(placeId, userId);
    }

    /**
     * 사용자의 좋아요 목록
     * @param json - {"user_id" : "asd"} 이와 같은 형식의 요청 데이터
     * @return List<PlaceLike> - [ {}, ...]
     */
    @PostMapping("/user-like-list")
    public List<ExtendedPlaceLike> userLikeList(@RequestBody JsonNode json){
        String userId = json.get("user_id").asText();
        return placeLikeService.getLikeListByUserId(userId);
    }

    /**
     * 특정 place의 좋아요 개수를 응답
     * @param json - {"place_id" : "123" } 형태의 요청
     * @return {"place_id":"1", "count" : "123"} 형태 응답
     */
    @RequestMapping("/count-place-like")
    public Map<String, String> countPlaceLike(@RequestBody JsonNode json){
        int placeId = json.get("place_id").asInt();
        int count = placeLikeService.countLikeByPlaceId(placeId);
        return Map.of(
                "place_id", placeId+"",
                "count", count+""
        );
    }

}
