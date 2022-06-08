package com.matjipdaehak.fo.security.authentication;

import com.matjipdaehak.fo.security.service.JwtService;
import com.matjipdaehak.fo.user.model.MatjipDaehakUserDetails;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtService jwtService;

    public JwtAuthenticationProvider(
            JwtService jwtService
    ){
        this.jwtService = jwtService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try{
            JwtAuthentication auth = (JwtAuthentication) authentication;
            final String jwt = auth.getCredentials();

            MatjipDaehakUserDetails userDetails =
                    jwtService.getUserDetailsFromJwt(jwt);

            auth.setUserDetails(userDetails);
            auth.setAuthenticated(true);
            return auth;
        }catch(Exception ex){
            throw new BadCredentialsException(ex.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthentication.class.isAssignableFrom(authentication);
    }

}
