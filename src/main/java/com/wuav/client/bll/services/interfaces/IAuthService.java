package com.wuav.client.bll.services.interfaces;

import com.wuav.client.be.user.AppUser;

import javax.naming.AuthenticationException;

public interface IAuthService {

    void authenticate(String email, String password) throws AuthenticationException;

}
