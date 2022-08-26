package com.matjipdaehak.fo.security.model;

/**
 * 비밀번호 변경시 강제 로그아웃등 jwt를 통제하는 기능 구현을 위해선
 * 서버쪽에서 jwt에 대한 정보를 갖고 있어야한다.
 *
 * 이 인터페이스는 해당 정보를 나타내는 객체의 표준인데,
 * 아무곳에서나 생성하지 못하도록 인터페이스화 함.
 * JwtService , JwtRepository에서 생성하도록 제한.
 */
public interface JwtInfo {

    /**
     * @return JWT발급시 생성되는 고유 아이디. 이것을 통해 각 Jwt를 구분한다.
     */
    long getJwtId();

    /**
     * @return 발급한 사용자의 id
     */
    String getUserId();

    /**
     * @return JWT 문자열
     */
    String getJwt();
}
