package com.wuav.client.bll.services.interfaces;

import com.wuav.client.be.user.AppUser;

import javax.naming.AuthenticationException;

public interface IAuthService {

    AppUser authenticate(String email, String password) throws AuthenticationException;

    boolean isAuthorized(AppUser user);

    String generateNewPassword(String email);
}
