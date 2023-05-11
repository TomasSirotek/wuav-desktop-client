package com.wuav.client.dal.mappers;
import com.wuav.client.be.user.AppUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    List<AppUser> getAllUsers();

    AppUser getUserById(@Param("userId")int id);

    AppUser getUserByEmail(@Param("email")String email);

    int createCustomer(AppUser appUser);

    int addUserToRole(@Param("userId")int userId, @Param("roleId") int roleId);

    int updateUser(@Param("userId") int userId,@Param("name") String userName,@Param("email") String email);

    int updateUserPasswordHash(@Param("userId") int id, @Param("passwordHash") String newPasswordHash);

    int createUser(@Param("userId") int id, @Param("userName") String name,@Param("email") String email, @Param("passwordHash")String password);

    int removeUserFromRole(@Param("userId") int userId);
}
