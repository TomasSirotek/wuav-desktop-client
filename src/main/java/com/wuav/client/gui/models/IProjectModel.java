package com.wuav.client.gui.models;

import com.wuav.client.be.Project;
import javafx.beans.property.ObjectProperty;

import java.util.List;

public interface IProjectModel {

    Project createProjectByName(int userId,String name);

    List<Project> getProjectByUserId(int userId);

    void setCurrentProject(Project project);

    ObjectProperty<Project> currentProjectProperty();

    Project getCurrentProject();

}
