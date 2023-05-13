package com.wuav.client.bll.services.interfaces;

import com.wuav.client.be.user.AppUser;

import java.util.List;

public interface IUserService {


    AppUser getUserByEmail(String email);

    List<AppUser> getAllUsers();

    AppUser getUserById(int id);

    int createCustomer(AppUser appUser);

    boolean updateUserById(AppUser appUser);

    boolean changeUserPasswordHash(int id, String newPasswordHash);
}
