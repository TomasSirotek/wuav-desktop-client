package com.wuav.client.bll.services;

import com.google.inject.Inject;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.services.interfaces.IAuthService;
import com.wuav.client.bll.services.interfaces.IUserService;
import com.wuav.client.bll.utilities.engines.cryptoEngine.ICryptoEngine;
import com.wuav.client.gui.models.user.CurrentUser;

import javax.naming.AuthenticationException;
import java.util.Random;

public class AuthService implements IAuthService {
    private IUserService userService;
    private final ICryptoEngine cryptoEngine;

    @Inject
    public AuthService(IUserService userService, ICryptoEngine cryptoEngine) {
        this.userService = userService;
        this.cryptoEngine = cryptoEngine;
    }

    @Override
    public void authenticate(String email, String password) throws AuthenticationException {
        AppUser user = userService.getUserByEmail(email);
        if (user == null) throw new AuthenticationException("User not found");
        boolean matchedPassword = cryptoEngine.HashCheck(user.getPasswordHash(), password);
        if(!matchedPassword)  throw new AuthenticationException("Invalid password");
        CurrentUser.getInstance().setLoggedUser(user);
    }
}