package com.matjipdaehak.fo.user.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.matjipdaehak.fo.user.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/user-management/signup")
public class SignupController {

    private final SignupService signupService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SignupController(
            SignupService signupService,
            PasswordEncoder passwordEncoder
    ){
        this.signupService = signupService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * { "email" : "...@......"} 요청에 대한 응답
     * @param reqBody - json data
     * @return
     */
    @PostMapping("/send-auth-code")
    public Map<String, String> sendAuthCode(@RequestBody Map<String, Object> reqBody, HttpServletResponse res) throws Exception{

        String email = reqBody.get("email").toString();

        if(!signupService.isEmailAddressPossible(email)){
            res.setStatus(412);
            return Map.of("message", "The email address is not possible");
        }

        signupService.sendAuthCodeToEmail(email);
        return Map.of("message","sended auth code");
    }

    /**
     * @param json: 요청 예시
     * {
     *     "email":"rst0070@uos.ac.kr",
     *     "auth_code":"719*GM>0"
     * }
     * @param res
     * @return
     */
    @RequestMapping("/check-auth-code")
    public Map<String, String> checkAuthCode(@RequestBody JsonNode json, HttpServletResponse res){
        String email = json.get("email").asText();
        String authCode = json.get("auth_code").asText();

        if(!signupService.checkAuthCode(email, authCode)){
            res.setStatus(406);
            return Map.of("message", "auth code is not matched");
        }
        return Map.of("message", "auth code is matched");
    }

    /**
     * { "user_id" : "....." } 방식의 요청에 해당 user-id가 유효한지 여부를 알려준다.
     * @return {} - 유효한 id인 경우 , {} - 유효하지 않은경우
     */
    @RequestMapping("/check-user-id")
    public Map<String, String> checkUserId(@RequestBody JsonNode json, HttpServletResponse res){
        String userId = json.get("user_id").asText();
        if(!signupService.isUserIdPossible(userId)){
            res.setStatus(406);
            return Map.of("message", "the id is not possible to use");
        }
        return Map.of("message", "id is possible to use");
    }

    /**
     * 학교 이메일 주소와 닉네임으로 요청하면 해당 닉네임이 해당 학교에서 사용가능한지 확인한다.
     * @param json
     * {
     *  "email" : "rst0070@uos.ac.kr",
     *  "nickname" : "asdasddd"
     * }
     * @return
     */
    @RequestMapping("/check-user-nickname")
    public Map<String, String> checkUserNickname(@RequestBody JsonNode json, HttpServletResponse res){
        String collegeEmailDomain = json.get("email").asText().split("@")[1];
        String userNickname = json.get("nickname").asText();
        if(!signupService.isUserNicknamePossible(userNickname, collegeEmailDomain)){
            res.setStatus(406);
            return Map.of("message", "the nickname is not possible to use");
        }
        return Map.of("message", "nickname is possible to use");
    }

    /**
     *
     * 인증코드확인
     * 아이디확인
     * 회원가입 기능 작동
     * @param json 요청 내용
     * {
     *      "user_id" :"",
     *      "password":"",
     *      "nickname":"",
     *      "auth_code":"",
     *      "email":""
     * }
     * @return
     */
    @PostMapping
    public Map<String, String> signupAction(@RequestBody JsonNode json, HttpServletResponse res){
        String emailAddr = json.get("email").asText();
        String authCode = json.get("auth_code").asText();
        String userId = json.get("user_id").asText();
        String password = json.get("password").asText();
        String nickname = json.get("nickname").asText();

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
