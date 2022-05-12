package com.matjipdaehak.fo.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtServiceImpl implements JwtService{

    /**
     * 시스템에서 사용될 jwt
     * exp는 numeric date사용
     */

    private final String SECRET_STRING = "b4P%YnnNOgl7:m({iJg?P|B4ND;-Yd";
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));
    private final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final long EXP_MINUTES = 60;

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
        String expStr = claims.get("exp").toString();
        LocalDateTime exp = LocalDateTime.parse(expStr, TIME_FORMAT);

        //jwt의 만료시각이 현재시각보다 앞설경우 false
        return exp.compareTo(LocalDateTime.now()) > 0;
    }

    @Override
    public String getJwtByUsername(String username){
        Map<String, String> claims = Map.of(
                "sub", username,
                "exp", getExp()
        );
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SECRET_KEY)
                .compact();
    }

    private String getExp(){
        return LocalDateTime.now().plusMinutes(EXP_MINUTES).format(TIME_FORMAT);
    }

}
