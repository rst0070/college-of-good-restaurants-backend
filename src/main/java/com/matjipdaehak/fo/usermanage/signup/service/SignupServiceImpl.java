package com.matjipdaehak.fo.usermanage.signup.service;

import com.matjipdaehak.fo.common.service.EmailService;
import com.matjipdaehak.fo.usermanage.signup.repository.SignupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.regex.*;

@Service
public class SignupServiceImpl implements SignupService{

    private final SignupRepository signupRepository;
    private final EmailService emailService;
    private final Pattern emailAddrPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    private final Random random = new Random();

    @Autowired
    public SignupServiceImpl(
            SignupRepository signupRepository,
            EmailService emailService
    ){
        this.signupRepository = signupRepository;
        this.emailService = emailService;
    }

    @Override
    public boolean isEmailAddressPossible(String emailAddress) {
        Matcher matcher = emailAddrPattern.matcher(emailAddress);
        if(!matcher.matches()) return false;
        String domain = emailAddress.split("@")[1];

        return signupRepository.isCollegeExistWithEmailDomain(domain)
                && !signupRepository.isUserExistWithEmail(emailAddress);
    }

    @Override
    public void sendAuthCodeToEmail(String emailAddress) {
        byte[] randomData = new byte[20];
        random.nextBytes(randomData);
        String authCode = new String(randomData, StandardCharsets.UTF_8);
        emailService.sendMessage(
                emailAddress,
                "맛집대학 인증코드",
                "인증코드: "+authCode
        );
    }

    @Override
    public boolean isUserIdPossible(String userId) {
        return false;
    }

    @Override
    public boolean checkAuthCode(String emailAddress, String authCode) {
        return false;
    }
}
