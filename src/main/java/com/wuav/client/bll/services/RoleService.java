package com.wuav.client.bll.services;

import com.google.inject.Inject;
import com.wuav.client.be.user.AppRole;
import com.wuav.client.bll.services.interfaces.IRoleService;
import com.wuav.client.dal.interfaces.IRoleRepository;
import com.wuav.client.dal.repository.RoleRepository;

public class RoleService implements IRoleService {

    private final IRoleRepository roleRepository;

    @Inject
    public RoleService(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public static void main(String[] args) {
        RoleService roleService = new RoleService(new RoleRepository());


        var result = roleService.getRoleByName("ADMIN");
        System.out.println(result);


    }
    @Override
    public AppRole getRoleByName(String name) {
        return roleRepository.getRoleByName(name);
    }
}
