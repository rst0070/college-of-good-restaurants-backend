package com.matjipdaehak.fo.security.service;

/**
 * 해당 서비스에서 다루는 jwt는 아래의 클래임을 갖게된다.
 * 1. sub - 사용자 아이디에 해당
 * 2. exp - 만료 시각
 */
public interface JwtService {

    /**
     * JWT의 유효성을 확인한다. 기능은 아래와 같다.
     * 1. 해당 문자열이 jwt인지(null인 경우도 확인)
     * 2. jwt인 경우 validation이 유효한지
     * 3. exp가 지나지 않았는지
     * @param jwtString
     * @return true - 유효한 jwt. false - 유효하지 않거나 jwt가 아닌경우
     */
    boolean checkJwtValidation(String jwtString);

    /**
     * username으로 jwt생성
     * @param username
     * @return jwt문자열
     */
    String getJwtByUsername(String username);
}
