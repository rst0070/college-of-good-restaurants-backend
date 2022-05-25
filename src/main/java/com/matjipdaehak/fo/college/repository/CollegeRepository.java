package com.matjipdaehak.fo.college.repository;

import com.matjipdaehak.fo.college.model.College;

/**
 * College 정보를 접근하는 repository 정의
 */
public interface CollegeRepository {

    College selectByCollegeMailDomain(String collegeMailDomain);
}
