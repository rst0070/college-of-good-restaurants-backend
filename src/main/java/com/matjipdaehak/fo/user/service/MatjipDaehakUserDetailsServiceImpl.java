package com.matjipdaehak.fo.user.service;

import com.matjipdaehak.fo.college.model.College;
import com.matjipdaehak.fo.college.service.CollegeService;
import com.matjipdaehak.fo.security.model.JwtInfo;
import com.matjipdaehak.fo.security.service.JwtService;
import com.matjipdaehak.fo.user.model.MatjipDaehakUserDetails;
import com.matjipdaehak.fo.user.repository.MatjipDaehakUserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MatjipDaehakUserDetailsServiceImpl implements MatjipDaehakUserDetailsService{

    private final MatjipDaehakUserDetailsRepository userDetailsRepository;
    private final CollegeService collegeService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MatjipDaehakUserDetailsServiceImpl(
            MatjipDaehakUserDetailsRepository userDetailsRepository,
            CollegeService collegeService,
            JwtService jwtService,
            PasswordEncoder passwordEncoder
    ){
        this.userDetailsRepository = userDetailsRepository;
        this.collegeService = collegeService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public MatjipDaehakUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDetailsRepository.selectUser(username);
    }

    @Override
    public MatjipDaehakUserDetails loadUserByJwt(String jwt) {
        JwtInfo jwtInfo = this.jwtService.getJwtInfoFromJwt(jwt);
        return this.loadUserByUsername(jwtInfo.getUserId());
    }

    @Override
    public boolean checkUsernamePassword(String username, String rawPassword){
        MatjipDaehakUserDetails userDetails = userDetailsRepository.selectUser(username);
        return passwordEncoder.matches(rawPassword, userDetails.getPassword());
    }

    @Override
    public void createNewUser(String username, String password, String nickname, String collegeEmailAddress) {
        //이메일 주소의 도메인을 이용해 학교가 어딘지 파악
        String collegeDomain = collegeEmailAddress.split("@")[1];
        College college = collegeService.getCollegeByEmailDomain(collegeDomain);
        //학교정보, 나머지 정보로 사용자 객체 생성
        MatjipDaehakUserDetails user = new MatjipDaehakUserDetails();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setCollegeId(college.getCollegeId());
        user.setCollegeEmailAddress(collegeEmailAddress);
        //사용자 객체를 DB에 저장
        userDetailsRepository.insertUser(user);
    }

    @Override
    public boolean isUserIdExist(String username) {
        return this.userDetailsRepository.isUserIdExist(username);
    }

    /**
     * changes password and save on DB.
     * also remove all jwts issued at previous times
     * @param username - 유저아이디
     * @param rawPassword - 인코딩되지 않은 새로운 패스워드
     */
    @Override
    public void changeUserPassword(String username, String rawPassword) {
        //load user information and encode new password
        MatjipDaehakUserDetails userDetails = this.loadUserByUsername(username);
        String password = passwordEncoder.encode(rawPassword);
        //set new password on Object
        userDetails.setPassword(password);

        //save changes on DB
        this.userDetailsRepository.updateUser(userDetails);
        //deprive the user of all JWT issued with previous password
        this.jwtService.depriveOfJwtByUserId(username);
    }

}
