package com.wuav.client.bll.strategies;

import com.google.inject.Inject;
import com.wuav.client.be.Project;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.strategies.interfaces.IUserRoleStrategy;
import com.wuav.client.gui.entities.DashboardData;
import com.wuav.client.gui.models.IProjectModel;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


/**
 * The strategy for the admin user role
 */
public class AdminStrategy implements IUserRoleStrategy {

    private final IProjectModel projectModel;

    private final String BUTTON_TEXT = "Projects";

    private final String BUTTON_TEXT_DASHBOARD = "View projects";
    private final String LABEL_TEXT_DASHBOARD = "Projects overview";

    private Image defaultImage = new Image(getClass().getClassLoader().getResource("admin.png").toExternalForm());

    /**
     * Constructor
     *
     * @param projectModel the project model
     */
    @Inject
    public AdminStrategy(IProjectModel projectModel) {
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
        return projectModel.getAllProjects();
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
        return projectModel.getAdminDashboardData(user.getId());
    }

    /**
     * Get boolean for button visibility
     *
     * @return boolean if the button is visible
     */
    @Override
    public boolean isUsersButtonVisible() {
        return true;
    }

    /**
     * Get String with button text
     *
     * @return
     */
    @Override
    public String getProjectButtonText() {
        return BUTTON_TEXT;
    }

    /**
     * Gets default user image for the user
     *
     * @return Image
     */
    @Override
    public Image getDefaultImage() {
        return defaultImage;
    }

    /**
     * Swaps the buttons
     *
     * @param exportToggleHbox 1st hbox
     * @param actionToggleHbox 2nd hbox
     */
    @Override
    public void swapButtons(HBox exportToggleHbox, HBox actionToggleHbox) {
        AtomicReference<MFXButton> storedButton = new AtomicReference<>();

        exportToggleHbox.getChildren().forEach(node -> {
            if (node instanceof MFXButton) {
                MFXButton button = (MFXButton) node;
                storedButton.set(button);
            }
        });
        actionToggleHbox.getChildren().clear();
        actionToggleHbox.getChildren().add(storedButton.get());
    }

    /**
     * Gets the dashboard button text
     *
     * @return the dashboard button text
     */
    @Override
    public String getDashboardButtonText() {
        return BUTTON_TEXT_DASHBOARD;
    }

    /**
     * Gets the dashboard main text
     *
     * @return the dashboard main text
     */
    @Override
    public String getDashboardMainText() {
        return LABEL_TEXT_DASHBOARD;
    }
}
