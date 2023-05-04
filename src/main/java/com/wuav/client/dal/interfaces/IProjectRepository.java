package com.wuav.client.dal.interfaces;

import com.wuav.client.be.CustomImage;
import com.wuav.client.be.Project;
import com.wuav.client.gui.dto.CreateProjectDTO;

import java.util.List;

public interface IProjectRepository {

    List<Project> getAllProjectsByUserId(int userId);

    Project getProjectById(int projectId);

    Project updateProject(int projectId, String description);

    int createProject(CreateProjectDTO projectDTO);

    int addProjectToUser(int userId, int projectId);



}
