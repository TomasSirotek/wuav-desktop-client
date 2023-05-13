package com.wuav.client.bll.services;

import com.google.inject.Inject;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.services.interfaces.IAuthService;
import com.wuav.client.bll.services.interfaces.IUserService;
import com.wuav.client.bll.utilities.engines.cryptoEngine.ICryptoEngine;
import com.wuav.client.gui.models.user.CurrentUser;

import javax.naming.AuthenticationException;

public class AuthService implements IAuthService {
    private IUserService userService;
    private final ICryptoEngine cryptoEngine;

    @Inject
    public AuthService(IUserService userService, ICryptoEngine cryptoEngine) {
        this.userService = userService;
        this.cryptoEngine = cryptoEngine;
    }

    @Override
    public AppUser authenticate(String email, String password) throws AuthenticationException {
        AppUser user = userService.getUserByEmail(email);
        var matchedPassword = cryptoEngine.HashCheck(user.getPasswordHash(),password);
        if (user != null && matchedPassword) {
            CurrentUser.getInstance().setLoggedUser(user);
            return user;
        } else {
            throw new AuthenticationException("Invalid email or password");
        }
    }

    @Override
    // Method to check whether the user is authorized
    public boolean isAuthorized(AppUser user) {
        return true; // user.getRoles().stream().anyMatch(role -> role.getName().equals("TECHNICIAN"));
    }
}