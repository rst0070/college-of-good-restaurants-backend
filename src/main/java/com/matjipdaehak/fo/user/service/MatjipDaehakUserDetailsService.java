package com.matjipdaehak.fo.user.service;

import com.matjipdaehak.fo.user.model.MatjipDaehakUserDetails;
import org.springframework.security.core.userdetails.*;

/**
 * 시스템 전체에서 UserDetails에 접근할때 사용하는 서비스 객체
 */
public interface MatjipDaehakUserDetailsService extends UserDetailsService {



    /**
     * user_id를 파라미터로 사용해서
     * MatjipDaehakUserDetails를 가져온다
     *
     * 내부적으로 MatjipDaehakUserDetailsRepository사용
     *
     * @param username - 사용자 id
     * @return MatjipDaehakUserDetails
     * @throws UsernameNotFoundException - 해당하는 사용자 id가 존재하지 않을때
     */
    @Override
    MatjipDaehakUserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    MatjipDaehakUserDetails loadUserByJwt(String jwt);

    /**
     * 특정 유저의 패스워드가 맞는지 확인한다.
     * @param username - 확인하려는 유저의 아이디.
     * @param rawPassword 인코딩 되지않은 순수한 패스워드 문자열
     * @return
     */
    boolean checkUsernamePassword(String username, String rawPassword);

    /**
     * 사용자를 DB에 생성한다.
     * @param username
     * @param password
     * @param collegeEmailAddress
     */
    void createNewUser(String username, String password, String nickname, String collegeEmailAddress);

    boolean isUserIdExist(String username);

    /**
     * 사용자의 패스워드를 변경한다.
     * 이때 기존의 패스워드로 로그인했던 JWT를 모두 무효화 해야하므로
     * JwtService에서 무효처리하도록 전달해야한다.
     * @param username - 유저아이디
     * @param rawPassword - 인코딩되지 않은 새로운 패스워드
     */
    void changeUserPassword(String username, String rawPassword);


}
