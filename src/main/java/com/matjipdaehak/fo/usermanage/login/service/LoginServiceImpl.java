package com.matjipdaehak.fo.usermanage.login.service;

import com.matjipdaehak.fo.security.service.JwtService;
import com.matjipdaehak.fo.userdetails.service.MatjipDaehakUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService{

    private final MatjipDaehakUserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Autowired
    public LoginServiceImpl(
            MatjipDaehakUserDetailsService userDetailsService,
            JwtService jwtService
    ){
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    public boolean checkUsernamePassword(String username, String password){
        return userDetailsService.checkUsernamePassword(username, password);
    }

    @Override
    public String getJwtByUsername(String username){
        return jwtService.getJwtByUsername(username);
    }
}
