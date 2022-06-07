package com.matjipdaehak.fo.college.service;

import com.matjipdaehak.fo.college.model.College;
import com.matjipdaehak.fo.college.repository.CollegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollegeServiceImpl implements CollegeService{

    private final CollegeRepository collegeRepository;

    @Autowired
    public CollegeServiceImpl(CollegeRepository collegeRepository){
        this.collegeRepository = collegeRepository;
    }

    @Override
    public College getCollegeByEmailDomain(String emailDomain) {
        return collegeRepository.selectByCollegeMailDomain(emailDomain);
    }

    @Override
    public College getCollegeByCollegeId(int collegeId){
        return collegeRepository.selectByCollegeId(collegeId);
    }
}
