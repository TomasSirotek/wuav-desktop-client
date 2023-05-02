package com.wuav.client.bll.services.interfaces;

import com.wuav.client.be.Project;

import java.io.File;
import java.util.List;

public interface IProjectService {
    Project createProjectByName(int userId,String name);

    List<Project> getProjectByUserId(int userId);

    boolean uploadImageWithDescription(int userId, int projectId, File file, String description, boolean isMainImage);
}
