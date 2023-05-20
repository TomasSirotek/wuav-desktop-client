package com.wuav.client.dal.mappers;

import com.wuav.client.be.Project;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IProjectMapper {

    List<Project> getAllProjectsByUserId(@Param("userId")int userId);

    List<Project> getAllProjects();

    Project getProjectById(@Param("id")int id);

    int createProject(@Param("id")int id,@Param("name")String name,@Param("description") String description,@Param("customerId") int customerId);

    int addUserToProject(@Param("userId") int userId,@Param("projectId") int projectId);

    // updating project with description
    int updateProjectForUserById(@Param("projectId")int projectId,@Param("description") String description);

    int updateNotes(@Param("projectId") int projectId,@Param("content") String content);

    int deleteProjectById(@Param("projectId") int projectId);

    int addDeviceToProject(@Param("projectId") int projectId, @Param("deviceId") int deviceId);

    int updateProjectName(@Param("projectId") int projectId,@Param("name") String newName);
}
