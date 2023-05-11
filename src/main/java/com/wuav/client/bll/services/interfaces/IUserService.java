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

    int createUser(String name, String email, String role);

    boolean updateUserRole(int id, String appRole);
}
