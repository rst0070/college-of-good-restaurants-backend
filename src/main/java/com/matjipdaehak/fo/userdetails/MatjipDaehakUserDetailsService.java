package com.matjipdaehak.fo.userdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

/**
 * 시스템 전체에서 UserDetails에 접근할때 사용하는 서비스 객체
 */
@Service
public class MatjipDaehakUserDetailsService implements UserDetailsService {

    private MatjipDaehakUserDetailsRepository userDetailsRepository;

    @Autowired
    public MatjipDaehakUserDetailsService(
            MatjipDaehakUserDetailsRepository userDetailsRepository){

        this.userDetailsRepository = userDetailsRepository;
    }

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        return userDetailsRepository.getUserDetailsByUsername(username);
    }
}
