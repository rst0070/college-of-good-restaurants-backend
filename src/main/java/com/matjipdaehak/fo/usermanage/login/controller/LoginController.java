package com.matjipdaehak.fo.usermanage.login.controller;

import com.matjipdaehak.fo.exception.AuthorizationException;
import com.matjipdaehak.fo.usermanage.login.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/user-management/login")
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService){
        this.loginService = loginService;
    }

    /**
     * {"jwt" : "jwt토큰내용"}방식으로 반환한다.
     * @param req
     * @return json 형식
     * @throws BadCredentialsException - 인증정보오류
     * @throws InternalAuthenticationServiceException - jwt생성중 오류
     */
    @RequestMapping
    public Map<String, String> loginAction(HttpServletRequest req) throws BadCredentialsException, InternalAuthenticationServiceException {

        final String authorizationHeader = req.getHeader("Authorization");
        //헤더 정보에 오류있을경우 예외 throw
        if(authorizationHeader == null || !authorizationHeader.startsWith("Basic ")) throw new BadCredentialsException("the authorization header has problems");


        String[] decoded;
        try{
            decoded = decodeBase64Str(authorizationHeader.substring(6)).split(":");
        }catch(Exception ex){throw new BadCredentialsException(ex.getMessage());}

        String username = decoded[0];
        String password = decoded[1];

        String jwt = loginService.getJwtByUsernamePassword(username, password);
        logger.info("user id: "+username+" issued JWT");
        return Map.of("jwt", jwt);
    }

    private String decodeBase64Str(String str){
        return new String(
                Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8
        );
    }

}
