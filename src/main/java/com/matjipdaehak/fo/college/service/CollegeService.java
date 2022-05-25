package com.matjipdaehak.fo.college.service;
import com.matjipdaehak.fo.college.model.College;

/**
 * College 데이터와 관련된 서비스 정의
 */
public interface CollegeService {

    College getCollegeByEmailDomain(String emailDomain);
}
