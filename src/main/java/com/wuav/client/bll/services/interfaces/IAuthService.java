package com.wuav.client.bll.services.interfaces;

import javax.naming.AuthenticationException;

/**
 * The interface for the authentication service
 */
public interface IAuthService {

    /**
     * Authenticate the user
     *
     * @param email
     * @param password
     * @throws AuthenticationException
     */
    void authenticate(String email, String password) throws AuthenticationException;
}
