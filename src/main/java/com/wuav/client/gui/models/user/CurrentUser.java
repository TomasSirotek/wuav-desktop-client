package com.wuav.client.gui.models.user;

import com.google.inject.Inject;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.services.interfaces.IUserService;

public class CurrentUser {

    private AppUser currentUser = null;
    private static volatile CurrentUser instance;



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

    public AppUser getLoggedUser() {
        return this.currentUser;
    }

    public void setLoggedUser(AppUser user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }
}
