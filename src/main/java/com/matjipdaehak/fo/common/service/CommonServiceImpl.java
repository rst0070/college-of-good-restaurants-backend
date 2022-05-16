package com.matjipdaehak.fo.common.service;

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
    public Map<String, Integer> getNumberOfStudentsInEachCollege(){
        return commonRepository.getNumberOfStudentsInEachCollege();
    }
}
