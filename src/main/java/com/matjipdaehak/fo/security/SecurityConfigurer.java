package com.matjipdaehak.fo.security;

import com.matjipdaehak.fo.security.authentication.JwtAuthenticationProvider;
import com.matjipdaehak.fo.security.filter.JwtAuthenticationFilter;
import com.matjipdaehak.fo.security.service.JwtService;
import com.matjipdaehak.fo.userdetails.service.MatjipDaehakUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter
implements WebMvcConfigurer {

    private final JwtService jwtService;

    @Autowired
    public SecurityConfigurer(
            JwtService jwtService
    ){
        super();
        this.jwtService = jwtService;
    }

    /**
     * cors허용 설정 일단 localhost:3000만 허용
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .exposedHeaders("Authorization")
                .allowCredentials(true)
                .allowedOrigins("https://localhost:3000");
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
        http
                .authorizeRequests()
                .antMatchers(
                        "/user-management/login/**",
                        "/user-management/signup/**"
                )
                .permitAll();
        http
                .antMatcher(
                        "/common/college-student-count**"
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(this.authenticationManagerBean()),
                        BasicAuthenticationFilter.class
                );
    }

    @Override
    public AuthenticationManager authenticationManagerBean(){
        return new ProviderManager(jwtAuthenticationProvider());
    }


    public AuthenticationProvider jwtAuthenticationProvider(){
        return new JwtAuthenticationProvider(jwtService);
    }
}
