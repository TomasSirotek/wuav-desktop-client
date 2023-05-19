package com.wuav.client.bll.services;

import com.google.inject.Inject;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.services.interfaces.IAuthService;
import com.wuav.client.bll.services.interfaces.IUserService;
import com.wuav.client.bll.strategies.UserRoleStrategyFactory;
import com.wuav.client.bll.strategies.interfaces.IUserRoleStrategy;
import com.wuav.client.bll.utilities.engines.cryptoEngine.ICryptoEngine;
import com.wuav.client.gui.models.user.CurrentUser;

import javax.naming.AuthenticationException;

public class AuthService implements IAuthService {
    private IUserService userService;
    private final ICryptoEngine cryptoEngine;

    private final UserRoleStrategyFactory userRoleStrategyFactory;

    @Inject
    public AuthService(IUserService userService, ICryptoEngine cryptoEngine, UserRoleStrategyFactory userRoleStrategyFactory) {
        this.userService = userService;
        this.cryptoEngine = cryptoEngine;
        this.userRoleStrategyFactory = userRoleStrategyFactory;
    }

    @Override
    public void authenticate(String email, String password) throws AuthenticationException {
        AppUser user = userService.getUserByEmail(email);
        if (user == null) throw new AuthenticationException("User not found"); // validate user
        boolean matchedPassword = cryptoEngine.HashCheck(user.getPasswordHash(), password); // hash check password
        if(!matchedPassword)  throw new AuthenticationException("Invalid password");
        CurrentUser.getInstance().setLoggedUser(user); // set current user
        IUserRoleStrategy userRoleStrategy = userRoleStrategyFactory.getStrategy(user); // get user role strategy
        CurrentUser.getInstance().setUserRoleStrategy(userRoleStrategy); // set user role strategy
    }
}