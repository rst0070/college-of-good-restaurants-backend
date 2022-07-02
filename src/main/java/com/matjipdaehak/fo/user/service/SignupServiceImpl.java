package com.matjipdaehak.fo.user.service;

import com.matjipdaehak.fo.college.service.CollegeService;
import com.matjipdaehak.fo.common.service.EmailService;
import com.matjipdaehak.fo.user.model.EmailAuthCode;
import com.matjipdaehak.fo.user.repository.SignupRepository;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.*;

@Service
public class SignupServiceImpl implements SignupService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final SignupRepository signupRepository;
    private final EmailService emailService;
    private final MatjipDaehakUserDetailsService userDetailsService;
    private final CollegeService collegeService;
    private final PasswordEncoder passwordEncoder;
    private final Pattern emailAddrPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");

    @Autowired
    public SignupServiceImpl(
            SignupRepository signupRepository,
            EmailService emailService,
            MatjipDaehakUserDetailsService userDetailsService,
            CollegeService collegeService,
            PasswordEncoder passwordEncoder
    ){
        this.signupRepository = signupRepository;
        this.emailService = emailService;
        this.userDetailsService = userDetailsService;
        this.collegeService = collegeService;
        this.passwordEncoder = passwordEncoder;
    }

    private Date getAuthCodeExpDate(){
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        c.add(Calendar.MINUTE, 20);
        return c.getTime();
    }

    @Override
    public boolean isEmailAddressPossible(String emailAddress) {
        Matcher matcher = emailAddrPattern.matcher(emailAddress);
        if(!matcher.matches()){
            logger.info("email addr not matched");
            return false;
        }
        String domain = emailAddress.split("@")[1];

        return signupRepository.isCollegeExistWithEmailDomain(domain)
                && !signupRepository.isEmailTakenByUser(emailAddress);
    }

    @Transactional
    @Override
    public void sendAuthCodeToEmail(String emailAddress){
        LocalDateTime ldt = LocalDateTime.now();
        long seed = ldt.getMinute() + ldt.getSecond() + ldt.getHour();
        Random random = new Random(seed);
        byte[] randomData = new byte[8];
        random.nextBytes(randomData);
        for(int i = 0; i < randomData.length; i++)
            randomData[i] = (byte)((randomData[i] % 25) + 65);//알파벳 대문자로 변환

        String authCode = new String(randomData, StandardCharsets.UTF_8);
        EmailAuthCode emailAuthCode = new EmailAuthCode(
                emailAddress,
                authCode,
                this.getAuthCodeExpDate()
        );

        emailService.sendMessage(
                emailAddress,
                "맛집대학 인증코드",
                "인증코드: "+authCode
        );

        if(signupRepository.isEmailTakenAsAuthCode(emailAddress)){
            signupRepository.updateEmailAuthCode(emailAuthCode);
        }else{
            signupRepository.insertEmailAuthCode(emailAuthCode);
        }
    }

    @Override
    public boolean isUserIdPossible(String userId) {
        return !signupRepository.isUserIdTaken(userId);
    }

    @Override
    public boolean isUserNicknamePossible(String nickname, String collegeEmailDomain) {
        int collegeId = collegeService.getCollegeByEmailDomain(collegeEmailDomain).getCollegeId();
        return !signupRepository.isUserNicknameTaken(nickname, collegeId);
    }

    @Override
    public boolean checkAuthCode(String emailAddress, String authCode) {
        EmailAuthCode inDb = signupRepository.selectEmailAuthCode(emailAddress);
        return authCode.equals(inDb.getAuthCode());
    }

    /**
     * MatjipDaehakUserdetailsService를 이용한다.
     * @param username - 사용자 id
     * @param rawPassword - password 원문
     * @param nickname
     * @param emailAddr - 사용자 학교 이메일 주소
     */
    @Override
    public void createNewUser(String username, String rawPassword, String nickname, String emailAddr) {
        String password = passwordEncoder.encode(rawPassword);
        userDetailsService.createNewUser(username, password, nickname, emailAddr);
    }
}
