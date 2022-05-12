package com.matjipdaehak.fo.userdetails.service;

import com.matjipdaehak.fo.userdetails.MatjipDaehakUserDetails;
import com.matjipdaehak.fo.userdetails.repository.MatjipDaehakUserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MatjipDaehakUserDetailsServiceImpl implements MatjipDaehakUserDetailsService{

    private final MatjipDaehakUserDetailsRepository userDetailsRepository;

    @Autowired
    public MatjipDaehakUserDetailsServiceImpl(
            MatjipDaehakUserDetailsRepository userDetailsRepository){

        this.userDetailsRepository = userDetailsRepository;
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
}
