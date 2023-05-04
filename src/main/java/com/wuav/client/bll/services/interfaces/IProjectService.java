package com.wuav.client.bll.services.interfaces;

import com.wuav.client.be.Project;
import com.wuav.client.gui.dto.CreateProjectDTO;

import java.io.File;
import java.util.List;

public interface IProjectService {

    List<Project> getProjectByUserId(int userId);

    boolean createProject(int userId, CreateProjectDTO projectToCreate);
}
