package com.matjipdaehak.fo.college.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.matjipdaehak.fo.college.model.College;
import com.matjipdaehak.fo.college.service.CollegeService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * college 정보 api
 */
@RestController
@RequestMapping("/college")
public class CollegeController {

    private final CollegeService collegeService;

    public CollegeController(CollegeService collegeService){
        this.collegeService = collegeService;
    }

    /**
     * x = longitude
     * y = latitude
     * radius = 거리제한(km)
     *
     * @param json : 요청 내용
     * 요청의 형태 - body로 요청
     *      * {"college_id" : "123123"}
     * @return
     * 응답의 형태
     *      * {
     *      *     "x":"128",
     *      *     "y":"37",
     *      *     "radiuds":"1"
     *      * }
     */
    @RequestMapping("/get-radius")
    public Map<String, String> getCollegeRadiusInfo(@RequestBody JsonNode json){
        int collegeId = json.get("college_id").asInt();
        College college = collegeService.getCollegeByCollegeId(collegeId);

        return Map.of(
                "x", college.getLongitude()+"",
                "y", college.getLatitude()+"",
                "radius", college.getDistanceLimitKm()+""
        );
    }

    /**
     * 학교 정보 객체의 내용을 모두 전달
     * 요청:
     * {
     *     "college_id" : "1"
     * }
     * @param json
     * @return
     */
    @PostMapping("/get-college")
    public College getCollege(@RequestBody JsonNode json){
        int collegeId = json.get("college_id").asInt();
        return this.collegeService.getCollegeByCollegeId(collegeId);
    }
}
