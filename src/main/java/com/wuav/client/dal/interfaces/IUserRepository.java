package com.wuav.client.dal.interfaces;

import com.wuav.client.be.user.AppUser;
import com.wuav.client.gui.dto.CreateUserDTO;

import java.util.List;

public interface IUserRepository {

    List<AppUser> getAllUsers();

    AppUser getUserById(int id);

    AppUser getUserByEmail(String email);

    int createCustomer(AppUser appUser);

    int addUserToRole(int userId, int roleId);

    boolean updateUserById(AppUser appUser);

    boolean changeUserPasswordHash(int id, String newPasswordHash);

    int createUser(CreateUserDTO createUserDTO);
}