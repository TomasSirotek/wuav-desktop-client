package com.wuav.client.bll.services;

import com.google.inject.Inject;
import com.wuav.client.be.user.AppRole;
import com.wuav.client.bll.services.interfaces.IRoleService;
import com.wuav.client.dal.interfaces.IRoleRepository;


/**
 * The implementation of the role service
 */
public class RoleService implements IRoleService {

    private final IRoleRepository roleRepository;

    /**
     * Constructor
     *
     * @param roleRepository the role repository
     */
    @Inject
    public RoleService(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Gets the role with the given name
     *
     * @param name the name of the role
     * @return the role
     */
    @Override
    public AppRole getRoleByName(String name) {
        return roleRepository.getRoleByName(name);
    }
}
