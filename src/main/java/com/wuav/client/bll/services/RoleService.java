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

    @Override
    public AppRole getRoleByName(String name) {
        return roleRepository.getRoleByName(name);
    }
}
