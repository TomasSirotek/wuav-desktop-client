package com.wuav.client.bll.services.interfaces;

import com.wuav.client.be.user.AppRole;

public interface IRoleService {

    AppRole getRoleByName(String name);
}
