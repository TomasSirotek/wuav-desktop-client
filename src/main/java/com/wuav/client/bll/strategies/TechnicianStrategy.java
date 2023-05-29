package com.wuav.client.bll.strategies;

import com.google.inject.Inject;
import com.wuav.client.be.Project;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.strategies.interfaces.IUserRoleStrategy;
import com.wuav.client.gui.entities.DashboardData;
import com.wuav.client.gui.models.IProjectModel;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

import java.util.List;

/**
 * The strategy for the technician user role
 */

public class TechnicianStrategy implements IUserRoleStrategy {

    private final IProjectModel projectModel;
    private final String BUTTON_TEXT = "My projects";

    private final String BUTTON_TEXT_DASHBOARD = "Create project";
    private final String LABEL_TEXT_DASHBOARD = "Start your projects";

    private Image defaultImage = new Image(getClass().getClassLoader().getResource("diceBar1.png").toExternalForm());

    /**
     * Constructor
     *
     * @param projectModel the project model
     */
    @Inject
    public TechnicianStrategy(IProjectModel projectModel) {
        this.projectModel = projectModel;
    }

    /**
     * Gets the projects of the given user
     *
     * @param user the user to get the projects for
     * @return a list of projects
     * @throws Exception if the projects could not be retrieved
     */
    @Override
    public List<Project> getProjects(AppUser user) throws Exception {
        return projectModel.getProjectsByUserId(user.getId());
    }

    /**
     * Gets the dashboard data of the given user
     *
     * @param user the user to get the dashboard data for
     * @return dashboard data model with the data
     * @throws Exception if the projects could not be retrieved
     */
    @Override
    public DashboardData getDashboardData(AppUser user) {
        return projectModel.getTechnicianDashboardData(user.getId());
    }

    /**
     * Gets the button text for the users button
     *
     * @return the button text
     */
    @Override
    public boolean isUsersButtonVisible() {
        return false;
    }

    /**
     * Gets the button text for the projects button
     *
     * @return the button text
     */
    @Override
    public String getProjectButtonText() {
        return BUTTON_TEXT;
    }

    /**
     * Gets the default image for the user
     *
     * @return the default image
     */
    @Override
    public Image getDefaultImage() {
        return defaultImage;
    }

    /**
     * Swaps the buttons in the given HBoxes
     *
     * @param exportToggleHbox the HBox containing the export button
     * @param actionToggleHbox the HBox containing the action button
     */
    @Override
    public void swapButtons(HBox exportToggleHbox, HBox actionToggleHbox) {
        // Do nothing
    }

    /**
     * Gets the button text for the dashboard button
     *
     * @return the button text
     */
    @Override
    public String getDashboardButtonText() {
        return BUTTON_TEXT_DASHBOARD;
    }

    /**
     * Gets the main text for the dashboard
     *
     * @return the main text
     */
    @Override
    public String getDashboardMainText() {
        return LABEL_TEXT_DASHBOARD;
    }
}