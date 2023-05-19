package com.wuav.client.bll.strategies.interfaces;

import com.wuav.client.be.Project;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.gui.entities.DashboardData;
import com.wuav.client.gui.models.ProjectModel;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

import java.util.List;

public interface IUserRoleStrategy {
    List<Project> getProjects(AppUser user) throws Exception;
    DashboardData getDashboardData(AppUser user);
    boolean isUsersButtonVisible();
    String getProjectButtonText();

    Image getDefaultImage();

    void swapButtons(HBox exportToggleHbox, HBox actionToggleHbox);

}
