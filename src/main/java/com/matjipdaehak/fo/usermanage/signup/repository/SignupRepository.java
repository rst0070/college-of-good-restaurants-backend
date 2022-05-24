package com.matjipdaehak.fo.usermanage.signup.repository;

import com.matjipdaehak.fo.usermanage.signup.model.EmailAuthCode;
import org.springframework.dao.DataAccessException;
import org.springframework.web.client.HttpClientErrorException;

public interface SignupRepository {

    /**
     * 학교 메일 도메인을 보내고 해당하는 학교가 db에 존재하는지 확인
     * @param emailDomain - college email domain. ex) "uos.ac.kr"
     * @return
     */
    boolean isCollegeExistWithEmailDomain(String emailDomain);

    /**
     * 해당하는 이메일 주소를 가진 사용자가 존재하는가
     * @param emailAddr -
     * @return true - 존재함. false - 없음
     */
    boolean isEmailTaken(String emailAddr);

    /**
     * 해당하는 userId가 이미 사용되었는가?
     * @param userId
     * @return
     */
    boolean isUserIdTaken(String userId);

    /**
     * Email Auth Code를 db에 저장
     * @param emailAuthCode - 저장하려는 email auth code
     * @throws DataAccessException - 데이터의 오류 or 이미 해당 코드가 존재하는 경루
     */
    void insertEmailAuthCode(EmailAuthCode emailAuthCode) throws DataAccessException;

    /**
     * 이메일 주소에 해당되는 인증코드객체를 가져온다.
     * @param emailAddr
     * @return EmailAuthCode - 성공시. null - 실패시(혹은 해당하는 객체가 없을시)
     */
    EmailAuthCode selectEmailAuthCode(String emailAddr);
}
