package com.matjipdaehak.fo.usermanage.login.service;

import com.matjipdaehak.fo.security.service.JwtService;
import com.matjipdaehak.fo.userdetails.MatjipDaehakUserDetails;
import com.matjipdaehak.fo.userdetails.service.MatjipDaehakUserDetailsService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public String getJwtByUsernamePassword(String username, String password) throws BadCredentialsException, InternalAuthenticationServiceException {
        try {
            MatjipDaehakUserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (!userDetails.getPassword().equals(password)) throw new BadCredentialsException("password incorrect");
        }catch(UsernameNotFoundException ex){
            throw new BadCredentialsException("there is no user identitied with " + username);
        }

        try{
            return Jwts.builder()
                    .setIssuedAt(jwtService.getDateNow())
                    .setExpiration(jwtService.getExpDate())
                    .setSubject(username)
                    .signWith(jwtService.getSecretKey())
                    .compact();
        }catch(Exception ex){
            throw new InternalAuthenticationServiceException(ex.getMessage());
        }
    }
}
