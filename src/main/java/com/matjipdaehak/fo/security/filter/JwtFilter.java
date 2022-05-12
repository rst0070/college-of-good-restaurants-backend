package com.matjipdaehak.fo.security.filter;

import com.matjipdaehak.fo.exception.AuthorizationException;
import com.matjipdaehak.fo.security.service.JwtService;
import com.matjipdaehak.fo.userdetails.service.MatjipDaehakUserDetailsService;
import com.matjipdaehak.fo.exception.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final MatjipDaehakUserDetailsService userDetailsService;

    @Autowired
    public JwtFilter(
            JwtService jwtService,
            MatjipDaehakUserDetailsService userDetailsService){
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * https://www.baeldung.com/spring-security-custom-filter
     * jwt 인증을 한다.
     * 해당 필터는 jwt를 이용한 인증이 필요한곳에서만 사용되므로
     * 1. 클라이언트에서 jwt를 보내지 않은 경우
     * 2. jwt에 오류가 있는경우
     * 위의 두가지 경우에 대해 모두 오류 코드를 보낸다.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
            ServletException, IOException, AuthorizationException{
        final String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader.startsWith("Bearer ") && jwtService.checkJwtValidation(authorizationHeader.substring(7))) {
            filterChain.doFilter(request, response);
            return;
        }
        throw new AuthorizationException();
    }
}
