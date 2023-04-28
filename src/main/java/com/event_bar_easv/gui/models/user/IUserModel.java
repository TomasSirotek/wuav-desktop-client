package com.event_bar_easv.gui.models.user;

import com.event_bar_easv.be.user.AppUser;
import javafx.collections.ObservableList;

public interface IUserModel {

    ObservableList<AppUser> getAllUsers();


    AppUser createUser(AppUser movie);


    AppUser getUserByEmail(String customerEmail);

    int createCustomerService(AppUser appUser);
}
