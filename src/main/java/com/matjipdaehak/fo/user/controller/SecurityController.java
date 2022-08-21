package com.matjipdaehak.fo.user.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.matjipdaehak.fo.security.service.JwtService;
import com.matjipdaehak.fo.user.model.MatjipDaehakUserDetails;
import com.matjipdaehak.fo.user.service.MatjipDaehakUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/user-management/security")
public class SecurityController {

    private final MatjipDaehakUserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Autowired
    public SecurityController(
            MatjipDaehakUserDetailsService userDetailsService,
            JwtService jwtService){
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    /**
     * 특정 유저의 패스워드를 변경한다.
     * @param json - 정보
     * @param req - jwt를 확인하기위해 필요
     * @param res
     */
    @PostMapping("/change-password")
    public void changePassword(@RequestBody JsonNode json, HttpServletRequest req, HttpServletResponse res){
        String jwt = req.getHeader("Authorization").toString().substring(7);
        MatjipDaehakUserDetails userDetails = this.jwtService.getUserDetailsFromJwt(jwt);

        String userId = json.get("user_id").asText();
        String rawOldPassword = new String(Base64.getDecoder().decode(json.get("old_password").asText()));
        String rawNewPassword = new String(Base64.getDecoder().decode(json.get("new_password").asText()));

        //바꿀려고하는 계정의 아이디와 jwt상의 유저아이디가 일치하는지 확인
        //아닐경우 권한오류
        if(!userDetails.getUsername().equals(userId)){
            res.setStatus(404);
            return;
        }

        //old password가 맞는지 확인한다.
        //아닐경우 잘못된 정보
        if(!this.userDetailsService.checkUsernamePassword(userId, rawOldPassword)){
            res.setStatus(406);
            return;
        }

        //패스워드 변경하기
        this.userDetailsService.changeUserPassword(userId, rawNewPassword);
    }
}
