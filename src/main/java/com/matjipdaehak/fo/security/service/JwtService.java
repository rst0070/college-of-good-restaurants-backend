package com.matjipdaehak.fo.security.service;


import com.matjipdaehak.fo.user.model.MatjipDaehakUserDetails;
import io.jsonwebtoken.JwtException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
/**
 * 시스템에서 자주 사용되는 jwt의 공통 속성들
 */
public interface JwtService {

    /**
     * MatjipDaehakUserDetails 객체를 이용해 해당하는 JWT를 생성한다.
     * @param userDetails - MatjipDaehakUserDetails 객체
     * @return
     * @throws JwtException
     */
    String createJwtWithUserDetails(MatjipDaehakUserDetails userDetails) throws JwtException;

    MatjipDaehakUserDetails getUserDetailsFromJwt(String jwt) throws JwtException, UsernameNotFoundException;

}
