package com.wuav.client.dal.interfaces;

import com.wuav.client.be.CustomImage;
import com.wuav.client.be.Project;

import java.util.List;

public interface IProjectRepository {

    List<Project> getAllProjectsByUserId(int userId);

    Project updateProject(int projectId, String description);

}
