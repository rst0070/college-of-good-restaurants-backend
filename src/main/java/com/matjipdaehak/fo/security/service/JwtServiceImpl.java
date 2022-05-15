package com.matjipdaehak.fo.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtServiceImpl implements JwtService{

    /**
     * 시스템에서 사용될 jwt
     * exp는 numeric date사용
     */

    private final String SECRET_STRING = "b4P%YnnNOgl7:m({iJg?P|B4ND;-Ydb4P%YnnNOgl7:m({iJg?P|B4ND;-Yd";
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));
    private final TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT");
    private final int EXP_MINUTES = 60;
    private final String ISSUER = "맛집대학";

    @Override
    public boolean checkJwtValidation(String jwtString){
        Jws<Claims> jws;
        try{
            jws = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(jwtString);
        }catch(JwtException ex){//signature 맞는지 || jwtString이 jwt형식이 맞는지 확인
            return false;
        }
        Claims claims = jws.getBody();

        if(!claims.getIssuer().equals(this.ISSUER)) return false;

        return claims.getExpiration().compareTo(this.getDateNow()) > 0;
    }

    @Override
    public String getJwtByUsername(String username){
        return Jwts.builder()
                .setIssuer(this.ISSUER)
                .setIssuedAt(getDateNow())
                .setExpiration(getExpDate())
                .setSubject(username)
                .signWith(SECRET_KEY)
                .compact();
    }

    private Date getExpDate(){
        Calendar calendar = Calendar.getInstance(TIME_ZONE);
        calendar.add(Calendar.MINUTE, EXP_MINUTES);
        return calendar.getTime();
    }

    private Date getDateNow(){
        return Calendar.getInstance(TIME_ZONE).getTime();
    }
}
