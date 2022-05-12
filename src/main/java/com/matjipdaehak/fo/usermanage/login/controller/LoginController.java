package com.matjipdaehak.fo.usermanage.login.controller;

import com.matjipdaehak.fo.exception.AuthorizationException;
import com.matjipdaehak.fo.usermanage.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/user-management/login")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService){
        this.loginService = loginService;
    }

    /**
     * {"jwt" : "jwt토큰내용"}방식으로 반환한다.
     * @param req
     * @return
     * @throws AuthorizationException - 로그인 정보등에 오류가 있을경우 예외처리한다.
     */
    @RequestMapping
    public Map<String, String> loginAction(HttpServletRequest req) throws AuthorizationException{

        final String authorizationHeader = req.getHeader("Authorization");
        //헤더 정보에 오류있을경우 예외 throw
        if(authorizationHeader == null || !authorizationHeader.startsWith("basic "))
            throw new AuthorizationException();

        String username = null;
        String password = null;
        try{
            String[] decoded = decodeBase64Str(authorizationHeader.substring(6)).split(":");
            username = decoded[0];
            password = decoded[1];

        }catch(Exception ex){//split연산, 배열 연산등에서 예외발생시
            throw new AuthorizationException("in com.matjipdaehak.fo.usermanage.login.LoginController:loginAction "+
                    "while decoding authorizationHeader");
        }

        //username, password 맞지 않을 경우 예외 throw
        if(!loginService.checkUsernamePassword(username, password))
            throw new AuthorizationException();

        String jwt = loginService.getJwtByUsername(username);
        return Map.of("jwt", jwt);
    }

    private String decodeBase64Str(String str){
        return new String(
                Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8
        );
    }

}
