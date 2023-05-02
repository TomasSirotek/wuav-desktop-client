package com.wuav.client.dal.interfaces;

import com.wuav.client.be.user.AppUser;

import java.util.List;

public interface IUserRepository {

    List<AppUser> getAllUsers();

    AppUser getUserById(int id);

    AppUser getUserByEmail(String email);

    int createCustomer(AppUser appUser);

    int addUserToRole(int userId, int roleId);
}