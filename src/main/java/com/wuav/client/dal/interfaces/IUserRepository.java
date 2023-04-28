package com.wuav.client.dal.interfaces;

import com.wuav.client.be.user.AppUser;

import java.util.List;

public interface IUserRepository {

    List<AppUser> getAllUsers();

    int createCustomer(AppUser appUser);

    int addUserToRole(int userId, int roleId);
}