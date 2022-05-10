package com.matjipdaehak.fo.userdetails;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@NoArgsConstructor
public class MatjipDaehakUserDetails implements UserDetails {


    private int collegeId;
    private String nickname;
    private String username;
    private String password;
    private String collegeEmailAddress;
    private List<GrantedAuthority> authorities;

    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

}
