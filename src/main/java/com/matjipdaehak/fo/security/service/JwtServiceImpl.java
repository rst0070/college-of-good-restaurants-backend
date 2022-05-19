package com.matjipdaehak.fo.security.service;

import com.matjipdaehak.fo.userdetails.MatjipDaehakUserDetails;
import com.matjipdaehak.fo.userdetails.service.MatjipDaehakUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtServiceImpl implements JwtService{

    /**
     * 시스템에서 사용될 jwt
     * exp는 numeric date사용
     */
    private final MatjipDaehakUserDetailsService userDetailsService;
    private final String SECRET_STRING = "b4P%YnnNOgl7:m({iJg?P|B4ND;-Ydb4P%YnnNOgl7:m({iJg?P|B4ND;-Yd";
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));
    private final TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT");
    private final int EXP_MINUTES = 60;
    private final String ISSUER = "맛집대학";

    @Autowired
    public JwtServiceImpl(MatjipDaehakUserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    private Date getDateNow(){
        return Calendar.getInstance(TIME_ZONE).getTime();
    }

    private Date getExpDate(){
        Calendar calendar = Calendar.getInstance(TIME_ZONE);
        calendar.add(Calendar.MINUTE, EXP_MINUTES);
        return calendar.getTime();
    }

    private Claims getClaimsFromJwt(String jwt) throws JwtException{
        return Jwts.parserBuilder()
                .setSigningKey(this.SECRET_KEY)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    @Override
    public String createJwtWithUserDetails(MatjipDaehakUserDetails userDetails) throws JwtException{
        Map<String, String> claims = Map.of(
                "username", userDetails.getUsername(),
                "college_id", userDetails.getCollegeId()+""
        );
        return Jwts.builder()
                .setIssuedAt(this.getDateNow())
                .setExpiration(this.getExpDate())
                .setClaims(claims)
                .signWith(this.SECRET_KEY)
                .compact();
    }

    @Override
    public MatjipDaehakUserDetails getUserDetailsFromJwt(String jwt) throws JwtException, UsernameNotFoundException {
        Claims claims = this.getClaimsFromJwt(jwt);
        return userDetailsService.loadUserByUsername(claims.get("username").toString());
    }



}
