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
        return commonRepository.getNumberOfStudentsInEachCollege();
    }

    @Override
    public long getCurrentDate() {
        return System.currentTimeMillis();
    }

    @Override
    public long getUniqueIdByCurrentDate() {
        long result = this.getCurrentDate() * 10000;
        long randomSalt = (long)(Math.random() * 10000);
        return result + randomSalt;
    }

    @Override
    public long getCurrentDateFromUniqueId(long uniqueId) {
        return uniqueId / 10000;
    }


}
