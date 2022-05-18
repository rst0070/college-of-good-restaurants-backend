package com.matjipdaehak.fo.usermanage.signup.controller;

import com.matjipdaehak.fo.usermanage.signup.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public Map<String, String> sendAuthCode(@RequestBody Map<String, Object> reqBody){

        String email = reqBody.get("email").toString();
        if(!signupService.isEmailAddressPossible(email)){
            return Map.of(
                    "status", "failed",
                    "message", "The email address is not possible"
            );
        }
        signupService.sendAuthCodeToEmail(email);
        return Map.of(
                "status", "success",
                "message","sended auth code"
        );
    }

    @RequestMapping("/check-auth-code")
    public Map<String, String> checkAuthCode(@RequestBody Map<String, String> reqMap){
        String email = reqMap.get("email");
        String authCode = reqMap.get("auth-code");
        signupService.checkAuthCode(email, authCode);
        return null;
    }

    /**
     * { "user-id" : "....." } 방식의 요청에 해당 user-id가 유효한지 여부를 알려준다.
     * @return {} - 유효한 id인 경우 , {} - 유효하지 않은경우
     */
    @RequestMapping("/check-user-id")
    public Map<String, String> checkUserId(HttpServletRequest req){
        return null;
    }

    @RequestMapping
    public Map<String, String> signupAction(HttpServletRequest req){
        return null;
    }
}
