package com.matjipdaehak.fo.usermanage.signup.repository;

import org.springframework.stereotype.Repository;

@Repository
public class SignupRepositoryImpl implements SignupRepository{

    @Override
    public boolean isCollegeExistWithEmailDomain(String emailDomain) {
        return true;
    }

    @Override
    public boolean isUserExistWithEmail(String emailAddr) {
        return false;
    }
}
