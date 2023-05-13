package com.wuav.client.dal.mappers;

import com.wuav.client.be.Address;
import com.wuav.client.be.user.AppRole;
import org.apache.ibatis.annotations.Param;

public interface RoleMapper {

    AppRole getRoleByName(@Param("roleName")String roleName);


}
