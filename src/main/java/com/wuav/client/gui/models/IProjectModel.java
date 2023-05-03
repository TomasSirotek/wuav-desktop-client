package com.wuav.client.gui.models;

import com.wuav.client.be.Project;
import javafx.beans.property.ObjectProperty;

import java.io.File;
import java.util.List;

public interface IProjectModel {

    Project createProjectByName(int userId,String name);

    List<Project> getProjectByUserId(int userId);

    boolean uploadImageWithDescription(int userId, int projectId, File file, String description, boolean isMainImage);


}
