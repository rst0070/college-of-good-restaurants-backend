package com.matjipdaehak.fo.security.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * JWT인증이 필요하지않은 요청에 대해서 인증정보를 저장하기 위한 객체
 * 특별한 인증이 필요하지 않은 상황에 사용되기 때문에 isAuthenticated 메소드가 항상 true를 리턴한다.
 */
public class WhiteListAuthentication implements Authentication {

    public WhiteListAuthentication(HttpServletRequest req){

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return null;
    }
}
