package com.wuav.client.dal.interfaces;

import com.wuav.client.be.user.AppRole;

/**
 * Interface for RoleRepository
 */
public interface IRoleRepository {

    /**
     * Gets role by name.
     *
     * @param name the name
     * @return the role by name
     */
    AppRole getRoleByName(String name);
}
