package com.wuav.client.bll.services.interfaces;

import com.wuav.client.be.user.AppRole;

/**
 * The interface for the role service
 */
public interface IRoleService {

    /**
     * Gets the role with the given name
     *
     * @param name the name of the role
     * @return the role
     */
    AppRole getRoleByName(String name);
}
