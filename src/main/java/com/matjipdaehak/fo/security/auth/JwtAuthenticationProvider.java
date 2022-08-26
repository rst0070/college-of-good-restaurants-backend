package com.matjipdaehak.fo.security.auth;

import com.matjipdaehak.fo.security.service.JwtService;
import com.matjipdaehak.fo.user.model.MatjipDaehakUserDetails;
import com.matjipdaehak.fo.user.service.MatjipDaehakUserDetailsService;
import io.jsonwebtoken.JwtException;
import org.slf4j.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Jwt인증을 수행하는 provider이다.
 * 인증중 오류 발생시 적절한 예외 메세지를 이용해 호출을 한 개체에게 넘겨주는것이 필요하다.
 */
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtService jwtService;
    private final MatjipDaehakUserDetailsService userDetailsService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public JwtAuthenticationProvider(
            JwtService jwtService,
            MatjipDaehakUserDetailsService userDetailsService
    ){
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * 인증처리를 한다. 예외가 발생하는 경우는 jwt문제, userdetail를 가져오는 중의 문제로 나뉠 수 있다.
     * @param authentication - 인증하려는 JwtAuthentication객체
     * @return 인증완료된 JwtAuthentication
     * @throws AuthenticationException - jwt만료등 jwt의 문제, UserDetails 객체를 가져올 수 없을때 혹은 가져오는중 문제가 발생시 예외가 발생한다.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try{
            JwtAuthentication auth = (JwtAuthentication) authentication;
            final String jwt = auth.getCredentials();

            //check validity: check from DB
            if(!this.jwtService.checkJwtValidity(jwt)) throw new InsufficientAuthenticationException("jwt가 올바르지 않음.");
            //get userdetails
            MatjipDaehakUserDetails userDetails =
                    userDetailsService.loadUserByJwt(jwt);

            auth.setUserDetails(userDetails);
            auth.setAuthenticated(true);
            return auth;
        }catch(JwtException je){
            throw new InsufficientAuthenticationException("jwt 인증중 jwt 형식, 혹은 만료기간에 문제가 있습니다.");
        }catch(UsernameNotFoundException ue){
            throw new UsernameNotFoundException("jwt 인증중 사용자 이름을 찾을 수 없습니다.");
        }
    }

    /**
     * 인자로 받은 형식의 Authentication이 맞는지 확인해준다.
     * 이 provider는 JwtAuthentication 형식만 지원
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthentication.class.isAssignableFrom(authentication);
    }

}
