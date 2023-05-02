package com.wuav.client.bll.services.interfaces;

import com.wuav.client.be.Project;

import java.util.List;

public interface IProjectService {
    Project createProjectByName(int userId,String name);

    List<Project> getProjectByUserId(int userId);
}
