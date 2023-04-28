package com.event_bar_easv.bll.services.interfaces;

import com.event_bar_easv.be.user.AppUser;

import java.util.List;

public interface IUserService {


    List<AppUser> getAllUsers();

    int createCustomer(AppUser appUser);
}
