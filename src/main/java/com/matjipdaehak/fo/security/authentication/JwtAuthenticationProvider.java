package com.matjipdaehak.fo.security.authentication;

import com.matjipdaehak.fo.security.service.JwtService;
import com.matjipdaehak.fo.userdetails.service.MatjipDaehakUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final MatjipDaehakUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public JwtAuthenticationProvider(
            MatjipDaehakUserDetailsService userDetailsService,
            JwtService jwtService
    ){
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try{
            JwtAuthentication auth = (JwtAuthentication) authentication;
            final String jwt = auth.getCredentials();

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtService.getSecretKey())
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            String username = claims.getSubject();
            if(jwtService.isExpired(claims.getExpiration())) throw new BadCredentialsException("JWT with user id - "+username+" expired");

            auth.setUserDetails(userDetailsService.loadUserByUsername(username));
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
