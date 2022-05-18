package com.matjipdaehak.fo.usermanage.signup.service;
import java.util.*;

public interface SignupService {

    /**
     * 해당 이메일 주소를 새로 등록할 수 있는지 확인
     * 1. 이메일 형식이 맞는가
     * 2. 해당하는 학교가 존재하는가
     * 3. 해당하는 주소가 유일한가
     * @param emailAddress - 확인할 이메일 주소
     * @return
     */
    boolean isEmailAddressPossible(String emailAddress);

    /**
     * user-id가 새로 생성 가능한지 확인
     * 1. 규칙에 맞는 아이디인가?
     * 2. 이미 존재하는 아이디인가?
     * @param userId - 확인하고자 하는 userid
     * @return true - 해당 아이디를 새로 생성가능. false - 새로 생성 불가능
     */
    boolean isUserIdPossible(String userId);

    void sendAuthCodeToEmail(String emailAddress);

    /**
     * 특정 이메일로 발송된 authentication code가 맞는지 확인한다.
     * @param emailAddress
     * @param authCode
     * @return
     */
    boolean checkAuthCode(String emailAddress, String authCode);


}
