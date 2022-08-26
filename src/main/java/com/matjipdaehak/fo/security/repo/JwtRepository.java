package com.matjipdaehak.fo.security.repo;

import com.matjipdaehak.fo.security.model.JwtInfo;

/**
 * JWT를 DB에서 조회, 수정, 저장, 삭제등을 하는 repository
 * redis를 이용할 예정이지만 임시적으로 기존 DB사용.
 */
public interface JwtRepository {

    //boolean isExist(long jwtId);

    void insertJwt(JwtInfo jwt);

    JwtInfo selectJwt(long jwtId);

    void deleteJwtByUserId(String userId);
}
