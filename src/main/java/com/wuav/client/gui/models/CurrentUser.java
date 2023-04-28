package com.wuav.client.gui.models;

import com.wuav.client.be.user.AppRole;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.services.interfaces.IUserService;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrentUser {
    private final static String PASSWORD = "1234";
    private static CurrentUser instance;
    private IUserService userService;
    private AppUser currentUser = null;
    private String userName;
    private String password;


    @Inject
    private CurrentUser() {

    }

    // Public static method to get the singleton instance
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

    // Public getter and setter methods for the name and password
    public AppUser getLoggedUser() {
        return this.currentUser;
    }

    public void login(String email) {
        this.currentUser = new AppUser();
        this.userName = userName;

        if(email.equals("admin@yahoo.com")){
            this.currentUser.setEmail("admin@yahoo.com");
            this.currentUser.setPasswordHash("dsfsdf");

            List<AppRole> appRoleList = new ArrayList<>();
            appRoleList.add(new AppRole(144784844,"administrator"));
            this.currentUser.setRoles(appRoleList);
        }
        else if(email.equals("cord@yahoo.com")){
            this.currentUser.setEmail("cord@yahoo.com");
            this.currentUser.setPasswordHash("dsfsdf");

            List<AppRole> appRoleList = new ArrayList<>();
            appRoleList.add(new AppRole(144784844,"coordinator"));
            this.currentUser.setRoles(appRoleList);
        }
        else{
            this.currentUser = null;
        }
    }

    public void logout() {
        this.currentUser = null;
        this.password = null;
    }

    // Method to check whether the user is authorized
    public boolean isAuthorized() {
       // AppUser user = userService.getUserByEmaul(userName);

        AppUser user = new AppUser();
        user.setEmail("admin");
        user.setPasswordHash("dsfsdf");
        if (Optional.ofNullable(user)
                .filter(u -> password.equals(PASSWORD))
                .isPresent()) {
            this.currentUser = user;
            return true;
        } else {
            this.currentUser = null;
            return false;
        }
    }
}