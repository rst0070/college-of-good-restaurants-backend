package com.matjipdaehak.fo.user.service;

import com.matjipdaehak.fo.college.model.College;
import com.matjipdaehak.fo.college.service.CollegeService;
import com.matjipdaehak.fo.user.model.MatjipDaehakUserDetails;
import com.matjipdaehak.fo.user.repository.MatjipDaehakUserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MatjipDaehakUserDetailsServiceImpl implements MatjipDaehakUserDetailsService{

    private final MatjipDaehakUserDetailsRepository userDetailsRepository;
    private final CollegeService collegeService;
    @Autowired
    public MatjipDaehakUserDetailsServiceImpl(
            MatjipDaehakUserDetailsRepository userDetailsRepository,
            CollegeService collegeService){
        this.userDetailsRepository = userDetailsRepository;
        this.collegeService = collegeService;
    }

    @Override
    public MatjipDaehakUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDetailsRepository.getUserDetailsByUsername(username);
    }

    @Override
    public boolean checkUsernamePassword(String username, String password){
        MatjipDaehakUserDetails userDetails = userDetailsRepository.getUserDetailsByUsername(username);
        return userDetails != null && userDetails.getPassword().equals(password);
    }

    @Override
    public void createNewUser(String username, String password, String nickname, String collegeEmailAddress) {
        String collegeDomain = collegeEmailAddress.split("@")[1];

        College college = collegeService.getCollegeByEmailDomain(collegeDomain);

        MatjipDaehakUserDetails user = new MatjipDaehakUserDetails();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setCollegeId(college.getCollegeId());
        user.setCollegeEmailAddress(collegeEmailAddress);

        userDetailsRepository.insertUser(user);
    }

}
