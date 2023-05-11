package com.wuav.client.bll.services;

import com.wuav.client.be.user.AppRole;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.services.interfaces.IRoleService;
import com.wuav.client.bll.services.interfaces.IUserService;
import com.wuav.client.bll.utilities.UniqueIdGenerator;
import com.wuav.client.bll.utilities.engines.cryptoEngine.CryptoEngine;
import com.wuav.client.bll.utilities.engines.cryptoEngine.ICryptoEngine;
import com.wuav.client.dal.interfaces.IUserRepository;
import com.google.inject.Inject;
import com.wuav.client.dal.repository.RoleRepository;
import com.wuav.client.dal.repository.UserRepository;
import com.wuav.client.gui.dto.CreateUserDTO;
import com.wuav.client.gui.models.user.CurrentUser;

import java.util.List;
import java.util.Random;

public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IRoleService roleService;


    private final ICryptoEngine cryptoEngine;



    @Inject
    public UserService(IUserRepository userRepository, IRoleService roleService, ICryptoEngine cryptoEngine) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.cryptoEngine = cryptoEngine;
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


    // THIS MIGHT BE REDUNDANT

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

    @Override
    public boolean updateUserById(AppUser appUser) {
        boolean isUpdated;

         isUpdated = userRepository.updateUserById(appUser);

        if(isUpdated){
            AppUser updatedUser = getUserById(appUser.getId());
            CurrentUser.getInstance().setLoggedUser(updatedUser);
        }

        return isUpdated;
    }

    @Override
    public boolean changeUserPasswordHash(int id, String newPasswordHash) {
        return userRepository.changeUserPasswordHash(id,newPasswordHash);
    }

    @Override
    public int createUser(String name, String email, String role) {

        AppUser existingUser = getUserByEmail(email);

        if(existingUser == null){
            // generate id
            int userId = UniqueIdGenerator.generateUniqueId();
            // generate new password
            String newPassword = generateRandomNumberAsString(8);
            // hash it
            String newPasswordHash = cryptoEngine.Hash(newPassword);

            // construct new user DTO

            CreateUserDTO createUserDTO = new CreateUserDTO(
                    userId,
                    name,
                    email,
                    newPasswordHash);

            int userResult = userRepository.createUser(createUserDTO);

            // if user is created successfully find role by role name and add user to role
            if(userResult > 0){
                AppRole appRole = roleService.getRoleByName(role);
                if(appRole != null){
                    int roleId = appRole.getId();
                    int userRoleResult = userRepository.addUserToRole(userId,roleId);
                    if(userRoleResult > 0){
                        return userResult;
                    }
                }
            }
        }
        return 0;
        // create new user

        // add user to role



    }

    private String generateRandomNumberAsString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }

        return sb.toString();
    }

}
