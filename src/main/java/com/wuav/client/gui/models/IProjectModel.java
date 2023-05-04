package com.wuav.client.gui.models;

import com.wuav.client.be.Project;
import com.wuav.client.gui.dto.CreateProjectDTO;
import javafx.beans.property.ObjectProperty;

import java.io.File;
import java.util.List;

public interface IProjectModel {

 //   Project createProjectByName(int userId,String name);

    List<Project> getProjectByUserId(int userId);

    boolean createProject(int userId,CreateProjectDTO projectToCreate);
}
