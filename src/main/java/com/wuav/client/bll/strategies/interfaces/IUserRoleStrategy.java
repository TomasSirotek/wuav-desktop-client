package com.wuav.client.bll.strategies.interfaces;

import com.wuav.client.be.Project;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.gui.entities.DashboardData;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

import java.util.List;

/**
 * The interface for the user role strategy
 */
public interface IUserRoleStrategy {
    /**
     * Gets the projects of the given user
     *
     * @param user the user to get the projects for
     * @return a list of projects
     * @throws Exception if the projects could not be retrieved
     */
    List<Project> getProjects(AppUser user) throws Exception;

    /**
     * Gets the dashboard data of the given user
     *
     * @param user the user to get the dashboard data for
     * @return dashboard data model with the data
     * @throws Exception if the projects could not be retrieved
     */
    DashboardData getDashboardData(AppUser user);

    /**
     * Get boolean for button visibility
     *
     * @return boolean if the button is visible
     */
    boolean isUsersButtonVisible();

    /**
     * Get String with button text
     *
     * @return
     */
    String getProjectButtonText();

    /**
     * Gets default user image for the user
     *
     * @return Image
     */
    Image getDefaultImage();

    /**
     * Swaps the buttons
     *
     * @param exportToggleHbox 1st hbox
     * @param actionToggleHbox 2nd hbox
     */
    void swapButtons(HBox exportToggleHbox, HBox actionToggleHbox);

    /**
     * Gets the dashboard button text
     *
     * @return String with button text
     */
    String getDashboardButtonText();

    /**
     * Gets the dashboard main text
     *
     * @return String with main text
     */
    String getDashboardMainText();
}
