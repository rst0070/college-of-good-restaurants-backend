package com.matjipdaehak.fo.security.service;

import javax.crypto.SecretKey;
import java.util.*;
/**
 * 시스템에서 자주 사용되는 jwt의 공통 속성들
 */
public interface JwtService {

    /**
     * jwt sign에 사용되는 key
     * @return SecretKey
     */
    SecretKey getSecretKey();

    /**
     * JWT의 exp속성을 전달하여 해당 토큰이 만료되었는지 확인한다.
     * ex) isExpired(claims.getExpiration());
     * @param exp - jwt의 exp속성
     * @return true - 시간이 만료된 경우, false - 만료되지 않은경우
     */
    boolean isExpired(Date exp);

    /**
     * @return Date - 현재 시간
     */
    Date getDateNow();

    /**
     * @return Date - jwt에 사용될 exp값
     */
    Date getExpDate();

}
