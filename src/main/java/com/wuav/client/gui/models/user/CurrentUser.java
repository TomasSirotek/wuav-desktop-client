package com.wuav.client.gui.models.user;

import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.strategies.interfaces.IUserRoleStrategy;

/**
 * The type Current user.
 */
public class CurrentUser {
    private AppUser currentUser = null;
    private IUserRoleStrategy userRoleStrategy = null;
    private static volatile CurrentUser instance;


    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static CurrentUser getInstance() {
        if (instance == null) {
            synchronized (CurrentUser.class) {
                if (instance == null) {
                    instance = new CurrentUser();
                }
            }
        }
        return instance;
    }

    /**
     * Gets logged user.
     *
     * @return the logged user
     */
    public AppUser getLoggedUser() {
        return this.currentUser;
    }

    /**
     * Sets logged user.
     *
     * @param user the user
     */
    public void setLoggedUser(AppUser user) {
        this.currentUser = user;
    }

    /**
     * Gets user role strategy.
     *
     * @return the user role strategy
     */
    public IUserRoleStrategy getUserRoleStrategy() {
        return this.userRoleStrategy;
    }

    /**
     * Sets user role strategy.
     *
     * @param userRoleStrategy the user role strategy
     */
    public void setUserRoleStrategy(IUserRoleStrategy userRoleStrategy) {
        this.userRoleStrategy = userRoleStrategy;
    }

    /**
     * Logout.
     */
    public void logout() {
        this.currentUser = null;
    }
}

