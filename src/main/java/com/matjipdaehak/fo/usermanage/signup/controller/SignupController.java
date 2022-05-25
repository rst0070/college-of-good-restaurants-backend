package com.matjipdaehak.fo.usermanage.signup.controller;
import com.matjipdaehak.fo.usermanage.signup.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/user-management/signup")
public class SignupController {

    private final SignupService signupService;

    @Autowired
    public SignupController(
            SignupService signupService
    ){
        this.signupService = signupService;
    }

    /**
     * { "email" : "...@......"} 요청에 대한 응답
     * @param reqBody - json data
     * @return
     */
    @PostMapping("/send-auth-code")
    public Map<String, String> sendAuthCode(@RequestBody Map<String, Object> reqBody, HttpServletResponse res){

        String email = reqBody.get("email").toString();

        if(!signupService.isEmailAddressPossible(email)){
            res.setStatus(412);
            return Map.of("message", "The email address is not possible");
        }

        if(!signupService.sendAuthCodeToEmail(email)){
            res.setStatus(500);
            return Map.of("message", "error occured while sending email");
        }

        return Map.of("message","sended auth code");
    }

    @RequestMapping("/check-auth-code")
    public Map<String, String> checkAuthCode(@RequestBody Map<String, String> reqMap, HttpServletResponse res){
        String email = reqMap.get("email");
        String authCode = reqMap.get("auth-code");
        if(!signupService.checkAuthCode(email, authCode)){
            res.setStatus(406);
            return Map.of("message", "auth code is not matched");
        }
        return Map.of("message", "auth code is matched");
    }

    /**
     * { "user-id" : "....." } 방식의 요청에 해당 user-id가 유효한지 여부를 알려준다.
     * @return {} - 유효한 id인 경우 , {} - 유효하지 않은경우
     */
    @RequestMapping("/check-user-id")
    public Map<String, String> checkUserId(@RequestBody Map<String, String> reqMap, HttpServletResponse res){
        String userId = reqMap.get("user-id");
        if(!signupService.isUserIdPossible(userId)){
            res.setStatus(406);
            return Map.of("message", "the id is not possible to use");
        }
        return Map.of("message", "id is possible to use");
    }

    /**
     * {
     *     "user-id" :"",
     *     "password":"",
     *     "nickname":"",
     *     "auth-code":"",
     *     "email":""
     * }
     * 인증코드확인
     * 아이디확인
     * 회원가입 기능 작동
     * @param reqMap
     * @return
     */
    @RequestMapping
    public Map<String, String> signupAction(@RequestBody Map<String, String> reqMap, HttpServletResponse res){
        String emailAddr = reqMap.get("email");
        String authCode = reqMap.get("auth-code");
        String userId = reqMap.get("user-id");
        String password = reqMap.get("password");
        String nickname = reqMap.get("nickname");

        if(!signupService.checkAuthCode(emailAddr, authCode)){
            res.setStatus(406);
            return Map.of("message", "인증코드 불일치");
        }
        if(!signupService.isUserIdPossible(userId)){
            res.setStatus(406);
            return Map.of("message", "해당 아이디 사용불가");
        }

        signupService.createNewUser(userId, password, nickname, emailAddr);

        return Map.of("status", "success");
    }
}
