package com.matjipdaehak.fo.security.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
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
    public SecretKey getSecretKey(){
        return SECRET_KEY;
    }

    @Override
    public boolean isExpired(Date exp) {
        return this.getDateNow().compareTo(exp) >= 0;
    }


    public Date getExpDate(){
        Calendar calendar = Calendar.getInstance(TIME_ZONE);
        calendar.add(Calendar.MINUTE, EXP_MINUTES);
        return calendar.getTime();
    }

    public Date getDateNow(){
        return Calendar.getInstance(TIME_ZONE).getTime();
    }
}
