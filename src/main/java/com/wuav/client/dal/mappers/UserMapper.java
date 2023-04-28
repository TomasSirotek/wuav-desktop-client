package com.wuav.client.dal.mappers;
import com.wuav.client.be.user.AppUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    List<AppUser> getAllUsers();


    int createCustomer(AppUser appUser);

    int addUserToRole(@Param("userId")int userId, @Param("roleId") int roleId);

}
