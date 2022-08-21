package com.matjipdaehak.fo.user.repository;

import com.matjipdaehak.fo.user.model.MatjipDaehakUserDetails;

public interface MatjipDaehakUserDetailsRepository {

    boolean isUserIdExist(String username);

    /**
     * @param username - 찾고자하는 사용자 아이디(USER.user_id 해당)
     * @return MatjipDaehakUserDetails. 만약 해당 아이디의 user가 없는 경우 null
     */
    MatjipDaehakUserDetails selectUser(String username);

    /**
     * 사용자를 DB에 등록한다.
     * @param userDetails - 사용자 정보 객체
     */
    void insertUser(MatjipDaehakUserDetails userDetails);

    /**
     * 기존에 존재하는 사용자의 정보를 수정한다.
     * 이때 파라미터 userDetails의 username은 DB에 존재하는것이어야 한다.
     * @param userDetails
     */
    void updateUser(MatjipDaehakUserDetails userDetails);
}
