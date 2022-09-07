package com.matjipdaehak.fo.security.service;

import com.matjipdaehak.fo.common.service.CommonService;
import com.matjipdaehak.fo.security.model.JwtInfo;
import com.matjipdaehak.fo.security.repo.JwtRepository;
import com.matjipdaehak.fo.user.model.MatjipDaehakUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.slf4j.*;

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
    private final JwtRepository jwtRepository;
    private final CommonService commonService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String SECRET_STRING = "b4P%YnnNOgl7:m({iJg?P|B4ND;-Ydb4P%YnnNOgl7:m({iJg?P|B4ND;-Yd";
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));
    private final TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT");
    private final String ISSUER = "맛집대학";

    @Autowired
    public JwtServiceImpl(
            JwtRepository jwtRepository,
            CommonService commonService
    ){
        this.jwtRepository = jwtRepository;
        this.commonService = commonService;
    }

    private Date getDateNow(){
        return Calendar.getInstance(TIME_ZONE).getTime();
    }

    private Date getExpDate(){
        Calendar calendar = Calendar.getInstance(TIME_ZONE);
        int EXP_MINUTES = 60;
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

    /**
     * JWT 문자열을 생성한다.
     * 필드의 내용
     * 1. Jwt의 아이디. 시간 정보 + 랜덤을 이용해 아이디 생성
     * 2. userid
     * 3. college_id
     * @param userDetails - MatjipDaehakUserDetails 객체
     * @return jwt 문자열
     */
    @Override
    public String createJwtWithUserDetails(MatjipDaehakUserDetails userDetails){
        //날짜 정보를 이용한 jwt의 고유 아이디.
        long jwtId = this.commonService.getUniqueIdByCurrentDate();

        //claims 설정
        Map<String, String> claims = Map.of(
                "jwt_id", jwtId+"",
                "username", userDetails.getUsername(),
                "college_id", userDetails.getCollegeId()+""
        );

        /*
          setClaims를 먼저 설정해줘야한다. 안그러면 그전의 데이터가 날라감.
         */
        final String jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(this.getDateNow())
                .setExpiration(this.getExpDate())
                .signWith(this.SECRET_KEY)
                .compact();

        //DB에 저장하기
        JwtInfo info = new JwtInf(jwtId, userDetails.getUsername(), jwt);
        this.jwtRepository.insertJwt(info);

        return jwt;
    }

    /**
     * 문자열을 통해 JwtInfo객체를 생성한다.
     * @param jwt - jwt 문자열
     * @return JwtInfo - 해당 문자열에서 추출한 jwt정보
     */
    @Override
    public JwtInfo getJwtInfoFromJwt(final String jwt) throws JwtException, NullPointerException{

        Claims claims = this.getClaimsFromJwt(jwt);
        final long jwtId = Long.parseLong(claims.get("jwt_id").toString());
        final String userId = claims.get("username").toString();

        return new JwtInf(jwtId, userId, jwt);
    }

    /**
     * jwt의 유효성을 확인한다.
     * 1. jwt에서 id를 읽는다. jwt에 오류가 있다면 여기서 걸러짐
     * 2. DB에서 jwt id로 jwt정보를 가져온다. <- 존재한다면 정상적인 jwt
     * @param jwt - jwt문자열
     * @return true - 유효한 jwt. false - 유효하지않은 jwt
     */
    @Override
    public boolean checkJwtValidity(String jwt) {
        try{
            JwtInfo info = this.getJwtInfoFromJwt(jwt);
            info = this.jwtRepository.selectJwt(info.getJwtId());

            if(info == null){
                logger.info("발급받지 않은 jwt로 인증시도");
                return false;
            }
        }catch(Exception e){
            return false;
        }

        return true;
    }

    /**
     * 특정 사용자 아이디의 JWT를 사용하지 못하도록 처리한다.
     * 사용하지 못하도록하는것은 DB에서 해당 JWT 정보를 삭제하는것
     *
     * @param userId
     */
    @Override
    public void depriveOfJwtByUserId(String userId) {
        this.jwtRepository.deleteJwtByUserId(userId);
    }


    private class JwtInf implements JwtInfo {

        private final long jwtId;
        private final String userId;
        private final String jwt;

        JwtInf(long jwtId, String userId, String jwt){
            this.jwtId = jwtId;
            this.userId = userId;
            this.jwt = jwt;
        }

        @Override
        public long getJwtId() {
            return jwtId;
        }

        @Override
        public String getUserId() {
            return userId;
        }

        @Override
        public String getJwt() {
            return jwt;
        }
    }
}
