package com.matjipdaehak.fo.security.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.matjipdaehak.fo.exception.CustomException;
import com.matjipdaehak.fo.exception.ErrorCode;
import com.matjipdaehak.fo.security.auth.JwtAuthentication;
import com.matjipdaehak.fo.security.auth.WhiteListAuthentication;
import org.slf4j.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.*;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedWriter;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** JWT인증이 필요한 URI패턴들 이때 String:matches메소드를 사용하므로 맞는 형식의 패턴사용해야함 */
    private final List<String> uriPatternToAuthenticate;

    private final AuthenticationManager authenticationManager;
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;

    /**
     * Jwt 인증 필터를 생성한다.<br/>
     * 해당 필터 생성시 어떠한 uri에 대해 필터링을 할지 정해주어야한다.
     * @param uriPatternToAuthenticate - jwt인증이 필요한 uri패턴들. 패턴의 형식은 https://codechacha.com/en/java-string-matches/참고
     * @param authenticationManager - JwtAuthentication객체의 유효성을 확인해줄 authentication manager(JwtAuthenticationProvider를 사용해야한다.)
     */
    public JwtAuthenticationFilter(
            List<String> uriPatternToAuthenticate,
            AuthenticationManager authenticationManager
    ){
        this.uriPatternToAuthenticate = uriPatternToAuthenticate;
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
        if(!this.isAuthenticationRequired(request)) {//jwt인증 불필요. WhiteListAuthentication으로 등록
            this.successfulAuthentication(request, response, filterChain, new WhiteListAuthentication(request));
            return;
        }

        // jwt 정보 헤더 확인
        // 헤더가 없을경우 null값으로 반환됨
        final String header = request.getHeader("Authorization");
        if(header == null || !header.startsWith("Bearer ")){
            this.unsuccessfulAuthentication(request, response,new CustomException(ErrorCode.AUTHORIZATION_HEADER_ERROR));
            return;
        }

        try{
            //jwt문자열
            final String JWT = header.substring(7);
            //jwt의 인증정보 가져오기
            JwtAuthentication authInfo = new JwtAuthentication(JWT);

            //정보가 맞는지 인증하기
            //인증시 문제가 발생하면 AuthenticationException이 발생한다.
            Authentication succeedAuth = authenticationManager.authenticate(authInfo);
            //인증성공시 SecurityContext에 인증정보등록 & 다음필터로 요청 전달.
            this.successfulAuthentication(request, response, filterChain, succeedAuth);

        }catch(AuthenticationException ex){
            this.unsuccessfulAuthentication(request, response, new CustomException(ErrorCode.INVALID_JWT));
        }
    }

    /**
     * JWT인증이 필요한 URI인지 확인한다.
     * @param req - 요청의 경로를 포함한 HttpServletRequest
     * @return true - 인증이 필요함. JwtAuthentication정보를 이용해 JwtAuthenticationProvider로 인증하고 정보를 SecurityContext에 등록필요.<br/>
     * false - 인증이 필요하지 않음. WhiteListAuthentication을 SecurityContext에 등록필요.
     */
    private boolean isAuthenticationRequired(HttpServletRequest req){
        final String URI = req.getRequestURI();
        //jwt 인증이 필요한가?
        boolean haveToJwtAuth = false;

        //해당 uri가 jwt인증이 필요한 목록의 패턴중 존재하는지 확인
        Iterator<String> uriListIt = this.uriPatternToAuthenticate.iterator();
        while(uriListIt.hasNext() && !haveToJwtAuth){
            haveToJwtAuth = URI.matches(uriListIt.next());
        }
        return haveToJwtAuth;
    }

    /**
     * 인증예외 발생시 인증이 안된것으로 처리 및 클라이언트에 대응한다.
     * 에러 리스폰스를 작성한다.
     * @param request
     * @param response
     * @param failed - 발생한 예외
     * @throws IOException
     * @throws ServletException
     */
    private void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, CustomException failed) throws IOException, ServletException, CustomException {
        SecurityContextHolder.clearContext();

        ObjectMapper mapper = new ObjectMapper();

        // create a JSON object
        ObjectNode errorBody = mapper.createObjectNode();
        errorBody.put("timestamp", (new Date(System.currentTimeMillis())).toString() );
        errorBody.put("status", failed.getRawStatusCode());
        errorBody.put("error", failed.getMessage());

        //send response
        response.setStatus(failed.getRawStatusCode());
        BufferedWriter bw = new BufferedWriter(response.getWriter());
        bw.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorBody));
        bw.flush();
        bw.close();
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
