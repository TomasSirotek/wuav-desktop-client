package com.wuav.client.bll.services.interfaces;

import com.wuav.client.be.user.AppUser;

import java.util.List;

public interface IUserService {


    List<AppUser> getAllUsers();

    int createCustomer(AppUser appUser);
}
