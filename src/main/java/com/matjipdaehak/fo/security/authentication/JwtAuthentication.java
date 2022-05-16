package com.matjipdaehak.fo.security.authentication;

import com.matjipdaehak.fo.userdetails.MatjipDaehakUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthentication implements Authentication {

    private final String JWT_STR;
    private boolean authenticated;
    private MatjipDaehakUserDetails userDetails;


    public JwtAuthentication(String jwt){
        this.JWT_STR = jwt;
        this.authenticated = false;
    }

    public void setUserDetails(MatjipDaehakUserDetails userDetails){
        this.userDetails = userDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    /**
     * 인증에 사용된 JWT문자열 반환
     * @return JWT 문자열
     */
    @Override
    public String getCredentials() {
        return JWT_STR;
    }

    @Override
    public MatjipDaehakUserDetails getDetails() {
        return this.userDetails;
    }


    @Override
    public String getPrincipal() {
        return userDetails.getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return this.getPrincipal();
    }
}
