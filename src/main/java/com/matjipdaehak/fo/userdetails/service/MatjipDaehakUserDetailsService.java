package com.matjipdaehak.fo.userdetails.service;

import com.matjipdaehak.fo.userdetails.MatjipDaehakUserDetails;
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

    boolean checkUsernamePassword(String username, String password);

}
