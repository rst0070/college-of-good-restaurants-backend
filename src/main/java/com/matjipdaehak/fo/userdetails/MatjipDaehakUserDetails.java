package com.matjipdaehak.fo.userdetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class MatjipDaehakUserDetails implements UserDetails {

    private List<GrantedAuthority> authorities;
    private String username;
    private String password;

    private boolean userExpired = false;
    private boolean credentialsExpired = false;
    private boolean locked = false;
    private boolean enabled = false;

    public MatjipDaehakUserDetails(
            String username,
            String password
    ){
        this.username = username;
        this.password = password;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.userExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
