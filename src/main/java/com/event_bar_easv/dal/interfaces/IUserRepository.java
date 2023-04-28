package com.event_bar_easv.dal.interfaces;

import com.event_bar_easv.be.user.AppUser;

import java.util.List;

public interface IUserRepository {

    List<AppUser> getAllUsers();

    int createCustomer(AppUser appUser);

    int addUserToRole(int userId, int roleId);
}