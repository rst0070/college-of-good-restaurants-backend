package com.matjipdaehak.fo.common.controller;

import com.matjipdaehak.fo.common.model.CollegeStudentCount;
import com.matjipdaehak.fo.common.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/common")
public class CommonController {

    private final CommonService commonService;

    @Autowired
    public CommonController(CommonService commonService){
        this.commonService = commonService;
    }

    /**
     * 전체 학교의 각 학생수를 json 형식으로 반환
     * 이름이 같은 학교가 있을까?? -> 있다면 문제발생
     * [
     *  {"collegeName" : "서울시립대학교", "numberOfStudents" : "100"},...
     * ]
     * @return Map<String, Integer> - 학교이름-학생수
     */
    @RequestMapping("/college-student-count")
    public List<CollegeStudentCount> numberOfStudentsInEachCollege(){
        return commonService.getNumberOfStudentsInEachCollege();
    }
}
