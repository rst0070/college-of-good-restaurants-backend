package com.matjipdaehak.fo.usermanage.signup.repository;

public interface SignupRepository {

    /**
     * 학교 메일 도메인을 보내고 해당하는 학교가 db에 존재하는지 확인
     * @param emailDomain - college email domain. ex) "uos.ac.kr"
     * @return
     */
    boolean isCollegeExistWithEmailDomain(String emailDomain);


    boolean isUserExistWithEmail(String emailAddr);
}
