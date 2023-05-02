package com.wuav.client.bll.services;

import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.services.interfaces.IUserService;
import com.wuav.client.dal.interfaces.IUserRepository;
import com.google.inject.Inject;
import com.wuav.client.dal.repository.UserRepository;

import java.util.List;

public class UserService implements IUserService {

    private final IUserRepository userRepository;


    @Inject
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public AppUser getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public List<AppUser> getAllUsers() {
        return userRepository.getAllUsers();
    }


    @Override
    public AppUser getUserById(int id) {
        AppUser appUser = userRepository.getUserById(id);
        System.out.println(appUser.toString());
        return appUser;
    }

    @Override
    public int createCustomer(AppUser appUser) {
        int result = 0;
        // create user
        var roleId = 300;

        result = userRepository.createCustomer(appUser);

        if(result > 0){
            result = userRepository.addUserToRole(appUser.getId(),roleId);
            // add user to role
        }

        return result;
    }

}
