package com.matjipdaehak.fo.security;

import com.matjipdaehak.fo.security.auth.JwtAuthenticationProvider;
import com.matjipdaehak.fo.security.filter.CORSFilter;
import com.matjipdaehak.fo.security.filter.JwtAuthenticationFilter;
import com.matjipdaehak.fo.security.service.JwtService;
import com.matjipdaehak.fo.user.service.MatjipDaehakUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.List;

@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final JwtService jwtService;
    private final MatjipDaehakUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfigurer(
            JwtService jwtService,
            MatjipDaehakUserDetailsService userDetailsService
    ){
        super();
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(WebSecurity web){
        //h2 console에 대한 작업은 시큐리티설정에서 제외
        web
                .ignoring()
                .antMatchers("/h2-console/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception{
        http
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        /**
         * 1.CORS필터적용
         * 2.JWT필터적용 (자체적으로 인증에 대한 whitelist를 가지고 있어 인증이 필요하지 않은경우 인증된것으로 처리)
         * 3.필터에서 인증되었다고 처리된 요청은 권한허용
         */
        http
                .addFilterBefore(new CORSFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(this.jwtAuthenticationFilter(), CORSFilter.class)
                .authorizeRequests()
                .anyRequest()
                .authenticated();
    }

    /**
     * 특정 URI에 대한 인증을 시도하는 JWT 필터를 생성
     * @return JwtAuthenticationFilter
     */
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        List<String> uriToAuthenticateList = List.of(
                "/review/add-review",
                "/place/add-place"
        );
        return new JwtAuthenticationFilter(
                uriToAuthenticateList,
                this.authenticationManager()
        );
    }

    @Override
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(this.jwtAuthenticationProvider());
    }

    @Bean
    public AuthenticationProvider jwtAuthenticationProvider(){
        return new JwtAuthenticationProvider(jwtService, userDetailsService);
    }
}
