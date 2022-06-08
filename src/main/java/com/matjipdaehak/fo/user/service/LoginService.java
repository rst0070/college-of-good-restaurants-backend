package com.matjipdaehak.fo.user.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

public interface LoginService {

    /**
     * username과 password를 이용해 jwt를 생성한다.
     * 이때 userdetails service를 이용해 username, password를 확인하며
     * 불일치 및 오류 발생시 Authentication exception을 발생시킨다.
     * @param username
     * @param rawPassword
     * @return jwt 문자열
     * @throws BadCredentialsException - username, password등에 문제 있을시
     * @throws InternalAuthenticationServiceException - jwt생성과정에 문제있을시
     */
    String getJwtByUsernamePassword(String username, String rawPassword) throws BadCredentialsException, InternalAuthenticationServiceException;
}
