package com.wuav.client.dal.mappers;

import com.wuav.client.be.Project;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectMapper {

    int createProjectByName(@Param("id")int id,@Param("name")String name,@Param("status") String status);

    void addUserToProject(@Param("userId") int userId,@Param("projectId") int projectId);

    List<Project> getAllProjectsByUserId(@Param("userId")int userId);

    Project getProjectById(@Param("id")int id);
}
