package com.matjipdaehak.fo.user.service;

import com.matjipdaehak.fo.security.PasswordConfig;
import com.matjipdaehak.fo.security.service.JwtService;
import com.matjipdaehak.fo.user.model.MatjipDaehakUserDetails;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class LoginServiceImpl implements LoginService {

    private final MatjipDaehakUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginServiceImpl(
            MatjipDaehakUserDetailsService userDetailsService,
            JwtService jwtService,
            PasswordEncoder passwordEncoder
    ){
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public String getJwtByUsernamePassword(String username, String rawPassword) throws BadCredentialsException, InternalAuthenticationServiceException {
        try {
            MatjipDaehakUserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (!passwordEncoder.matches(rawPassword, userDetails.getPassword())) throw new BadCredentialsException("password incorrect");

            return jwtService.createJwtWithUserDetails(userDetails);
        }catch(UsernameNotFoundException ex){
            throw new BadCredentialsException("there is no user identitied with " + username);
        }catch(JwtException ex){
            throw new InternalAuthenticationServiceException(ex.getMessage());
        }
    }
}
