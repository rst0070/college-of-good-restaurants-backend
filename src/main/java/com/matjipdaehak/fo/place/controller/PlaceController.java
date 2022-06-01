package com.matjipdaehak.fo.place.controller;

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

    @Autowired
    public PlaceController(PlaceService placeService){
        this.placeService = placeService;
    }

    /**
     * 예시 데이터
     * {
     * 	"category_name": "테마파크",
     * 	"kakao_id": "22225498",
     * 	"phone": "0507-1352-1401",
     * 	"place_name": "산토끼노래동산",
     * 	"place_url": "http://place.map.kakao.com/22225498",
     * 	"road_address_name": "경남 창녕군 이방면 이방로 623",
     * 	"x": "128.383585226179",
     * 	"y": "35.5777087822771"
     * }
     * 예외를 세분화해 처리할 필요가 있음. ex. 서버 내부오류 or 입력데이터 오류
     * @param req
     * @param res
     */
    @RequestMapping("/add-place")
    public Map<String, String> addPlace(@RequestBody Map<String, String> req, HttpServletResponse res){
        try{
            Place place = new Place(
                    req.get("kakao_id"),
                    req.get("place_name"),
                    req.get("road_address_name"),
                    Double.parseDouble(req.get("y")),
                    Double.parseDouble(req.get("x")),
                    req.get("phone"),
                    req.get("category_name")
            );
            placeService.createNewPlace(place);
        }catch(Exception ex){
            logger.info(ex.getMessage());
            res.setStatus(500);
            return Map.of("message", ex.getMessage());
        }
        return null;
    }
}
