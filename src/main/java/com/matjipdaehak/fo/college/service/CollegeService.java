package com.matjipdaehak.fo.college.service;
import com.matjipdaehak.fo.college.model.College;

/**
 * College 데이터와 관련된 서비스 정의
 */
public interface CollegeService {

    /**
     * 이메일 도메인으로 학교객체를 가져온다.
     * @param emailDomain
     * @return
     */
    College getCollegeByEmailDomain(String emailDomain);

    /**
     * 학교 아이디로 학교객체를 가져온다.
     * @param collegeId
     * @return
     */
    College getCollegeByCollegeId(int collegeId);
}
