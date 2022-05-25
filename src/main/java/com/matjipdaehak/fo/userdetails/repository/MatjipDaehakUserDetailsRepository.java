package com.matjipdaehak.fo.userdetails.repository;

import com.matjipdaehak.fo.userdetails.MatjipDaehakUserDetails;

public interface MatjipDaehakUserDetailsRepository {

    /**
     * @param username - 찾고자하는 사용자 아이디(USER.user_id 해당)
     * @return MatjipDaehakUserDetails. 만약 해당 아이디의 user가 없는 경우 null
     */
    MatjipDaehakUserDetails getUserDetailsByUsername(String username);

    /**
     * 사용자를 DB에 등록한다.
     * @param userDetails - 사용자 정보 객체
     */
    void insertUser(MatjipDaehakUserDetails userDetails);
}
