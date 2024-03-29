package com.matjipdaehak.fo.user.service;

public interface SignupService {

    /**
     * 해당 이메일 주소를 새로 등록할 수 있는지 확인
     * 1. 이메일 형식이 맞는가
     * 2. 해당하는 학교가 존재하는가
     * 3. 해당하는 주소가 유일한가
     * @param emailAddress - 확인할 이메일 주소
     * @return true - 해당 이메일 사용가능. false - 사용불가
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

    /**
     * 닉네임이 사용가능한지 확인
     * @param nickname
     * @param collegeEmailDomain
     * @return
     */
    boolean isUserNicknamePossible(String nickname, String collegeEmailDomain);

    /**
     * 인증코드 이메일 발송. 인증코드는 랜덤으로 생성되며 DB에 저장됨
     * @param emailAddress
     * @return true - 발송 성공. false - 발송 실패
     */
    void sendAuthCodeToEmail(String emailAddress);

    /**
     * 특정 이메일로 발송된 authentication code가 맞는지 확인한다.
     * null point exception 등 확인할 필요있음
     * @param emailAddress
     * @param authCode
     * @return true - 인증코드 맞음. false - 인증코드 맞지 않음
     */
    boolean checkAuthCode(String emailAddress, String authCode);

    /**
     * 이미 가입이 가능한지 모두 확인한 상태에서 DB에 정보만 입력하는 기능
     * @param username
     * @param rawPassword
     * @param emailAddr - 사용자 학교 이메일 주소
     */
    void createNewUser(String username, String rawPassword, String nickname, String emailAddr);
}
