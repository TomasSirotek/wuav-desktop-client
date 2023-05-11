package com.wuav.client.gui.models.user;

import com.wuav.client.be.user.AppUser;
import javafx.collections.ObservableList;

public interface IUserModel {

    ObservableList<AppUser> getAllUsers();


    AppUser createUser(AppUser movie);


    AppUser getUserByEmail(String customerEmail);

    int createCustomerService(AppUser appUser);

    boolean updateUserById(AppUser appUser);
}
