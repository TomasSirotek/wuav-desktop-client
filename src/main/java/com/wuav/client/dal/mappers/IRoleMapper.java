package com.wuav.client.dal.mappers;

import com.wuav.client.be.user.AppRole;
import org.apache.ibatis.annotations.Param;

public interface IRoleMapper {

    AppRole getRoleByName(@Param("roleName")String roleName);


}
