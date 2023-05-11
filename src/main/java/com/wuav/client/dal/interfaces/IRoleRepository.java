package com.wuav.client.dal.interfaces;

import com.wuav.client.be.user.AppRole;

public interface IRoleRepository {

    AppRole getRoleByName(String name);
}
