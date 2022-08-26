package com.matjipdaehak.fo.security.service;


import com.matjipdaehak.fo.security.model.JwtInfo;
import com.matjipdaehak.fo.user.model.MatjipDaehakUserDetails;
import io.jsonwebtoken.JwtException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
/**
 * 시스템에서 자주 사용되는 jwt의 공통 속성들 및 memory DB에 JWT정보를 저장한다.
 */
public interface JwtService {

    /**
     * MatjipDaehakUserDetails 객체를 이용해 해당하는 JWT를 생성한다.
     * 또한 해당 정보를 DB에 저장한다.
     * @param userDetails - MatjipDaehakUserDetails 객체
     * @return
     * @throws JwtException
     */
    String createJwtWithUserDetails(MatjipDaehakUserDetails userDetails) throws JwtException;

    JwtInfo getJwtInfoFromJwt(String jwt);

    /**
     * Jwt의 유효성을 확인한다.
     * JwtAuthenticationProvider에서 사용해야함.
     * 유효성 확인은 서버측에 저장된 Jwt id를 확인하는등의 작업을 한다.
     * @param jwt
     * @return
     */
    boolean checkJwtValidity(String jwt);

    /**
     * 특정 사용자 아이디의 JWT를 사용하지 못하도록 처리한다.
     * 사용하지 못하도록하는것은 DB에서 해당 JWT 정보를 삭제하는것
     * @param userId
     */
    void depriveOfJwtByUserId(String userId);

    //MatjipDaehakUserDetails getUserDetailsFromJwt(String jwt) throws JwtException, UsernameNotFoundException;

}
