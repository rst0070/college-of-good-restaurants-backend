package com.matjipdaehak.fo.security.filter;

import com.matjipdaehak.fo.security.authentication.JwtAuthentication;
import org.slf4j.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.*;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AuthenticationManager authenticationManager;
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
        this.successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        this.failureHandler = new AuthenticationEntryPointFailureHandler(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }

    /**
     * Authorization Header를 확인하여 JWT를 읽는다.
     * Authentication Manager를 통해서 읽은 JWT로 만든 Authentication이 유효한지 확인하고
     * 유효하다면 successfulAuthentication
     * 유효하지 않다면 unsuccessfulAuthentication
     * 호출
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            final String header = request.getHeader("Authorization");
            if(header == null || !header.startsWith("Bearer ")) throw new BadCredentialsException("type of Authorization header has problem");
            final String JWT = header.substring(7);

            JwtAuthentication authInfo = new JwtAuthentication(JWT);

            Authentication succeedAuth = authenticationManager.authenticate(authInfo);

            this.successfulAuthentication(request, response, filterChain, succeedAuth);
        }catch(AuthenticationException ex){
            logger.warn(ex.getMessage());
            this.unsuccessfulAuthentication(request, response, ex);
        }
    }

    /**
     * 인증예외 발생시 인증이 안된것으로 처리 및 클라이언트에 자동으로 대응, 이벤트 리스너에 전달
     * @param request
     * @param response
     * @param failed - 발생한 예외
     * @throws IOException
     * @throws ServletException
     */
    private void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        this.failureHandler.onAuthenticationFailure(request, response, failed);
    }

    /**
     * 인증성공시 처리, 이벤트리스너에 전달
     * @param request
     * @param response
     * @param chain
     * @param authentication - 인증완료된 인증객체
     * @throws IOException
     * @throws ServletException
     */
    private void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
        //this.successHandler.onAuthenticationSuccess(request, response, chain, authentication);
    }
}
