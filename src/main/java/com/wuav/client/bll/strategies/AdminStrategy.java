package com.wuav.client.bll.strategies;

import com.google.inject.Inject;
import com.wuav.client.be.Project;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.strategies.interfaces.IUserRoleStrategy;
import com.wuav.client.gui.entities.DashboardData;
import com.wuav.client.gui.models.IProjectModel;
import java.util.List;

public class AdminStrategy implements IUserRoleStrategy {

    private final IProjectModel projectModel;

    @Inject
    public AdminStrategy(IProjectModel projectModel) {
        this.projectModel = projectModel;
    }

    @Override
    public List<Project> getProjects(AppUser user) throws Exception {
        return projectModel.getAllProjects();
    }

    @Override
    public DashboardData getDashboardData(AppUser user) throws Exception {
        return projectModel.getTechnicianDashboardData(user.getId());
    }
}
