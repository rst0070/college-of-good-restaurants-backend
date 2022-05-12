package com.matjipdaehak.fo.usermanage.login.service;

public interface LoginService {

    boolean checkUsernamePassword(String username, String password);

    String getJwtByUsername(String username);
}
