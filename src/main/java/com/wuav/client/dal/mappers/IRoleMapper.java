package com.wuav.client.dal.mappers;

import com.wuav.client.be.user.AppRole;
import org.apache.ibatis.annotations.Param;

/**
 * interface IRoleMapper
 **/
public interface IRoleMapper {

    /**
     * Gets role by role name (roleName)
     *
     * @param roleName roleName
     * @return AppRole
     */
    AppRole getRoleByName(@Param("roleName") String roleName);

}
