package com.matjipdaehak.fo.common.service;

import com.matjipdaehak.fo.common.model.CollegeStudentCount;
import com.matjipdaehak.fo.common.repository.CommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CommonServiceImpl implements CommonService{

    private final CommonRepository commonRepository;

    @Autowired
    public CommonServiceImpl(CommonRepository commonRepository){
        this.commonRepository = commonRepository;
    }


    @Override
    public List<CollegeStudentCount> getNumberOfStudentsInEachCollege(){
        Map<String, Integer> collegeMap =  commonRepository.getNumberOfStudentsInEachCollege();
        List<CollegeStudentCount> list = new LinkedList<CollegeStudentCount>();
        collegeMap.forEach((String collegeName, Integer count) ->{
            CollegeStudentCount csc = new CollegeStudentCount();
            csc.setCollegeName(collegeName);
            csc.setNumberOfStudents(count);
            list.add(csc);
        });
        return list;
    }
}
