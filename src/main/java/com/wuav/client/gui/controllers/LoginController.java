package com.wuav.client.gui.controllers;

import com.wuav.client.be.Project;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.bll.services.interfaces.IAuthService;
import com.wuav.client.bll.strategies.interfaces.IUserRoleStrategy;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import com.google.inject.Inject;
import com.wuav.client.gui.entities.DashboardData;
import com.wuav.client.gui.manager.StageManager;
import com.wuav.client.gui.models.user.CurrentUser;
import com.wuav.client.gui.utils.AnimationUtil;
import com.wuav.client.gui.utils.enums.CustomColor;
import io.github.palexdev.materialfx.controls.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginController extends RootController implements Initializable {
    @FXML
    private Pane errorPane, loadingPane;
    @FXML
    private MFXProgressSpinner progressLoader;
    @FXML
    private MFXPasswordField userPswField;
    @FXML
    private MFXTextField userEmailField;
    private final IControllerFactory controllerFactory;

    private final IAuthService authService;

    private final StageManager stageManager;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Inject
    public LoginController(IControllerFactory controllerFactory, IAuthService authService, StageManager stageManager) {
        this.controllerFactory = controllerFactory;
        this.authService = authService;
        this.stageManager = stageManager;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    /**
     * Load the new view
     * @throws IOException
     */
    @FXML
    private void login() {
        // Show the progress bar while the application is loading
        progressLoader.setVisible(true);
        loadingPane.setVisible(true);
        loadingPane.setStyle(CustomColor.DIMMED.getStyle());

        executorService.submit(() -> {
            try {
                // Authenticate the user and check authorization
                authService.authenticate(userEmailField.getText(), userPswField.getText());

                // Fetch all projects for the user
                AppUser user = CurrentUser.getInstance().getLoggedUser();
                IUserRoleStrategy strategy = CurrentUser.getInstance().getUserRoleStrategy();
                try {
                    strategy.getProjects(user);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                // Update the UI on the JavaFX application thread
                Platform.runLater(() -> {
                    try {
                        // Hide the progress bar
                        progressLoader.setVisible(false);
                        loadingPane.setStyle(CustomColor.TRANSPARENT.getStyle());

                        getStage().close(); // Close the login stage
                        loadNewView(); // Load the new view
                        executorService.shutdown(); // Shutdown the executor service
                    } catch (IOException e) {
                        errorPane.setVisible(true);
                    }
                });
            } catch (AuthenticationException e) {
                // Update the UI on the JavaFX application thread
                Platform.runLater(() -> {
                    handleError();
                });
            }
        });
    }

    /**
     * Load the new view
     * @throws IOException
     */
    private void loadNewView() throws IOException {
        String stageTitle = "WUAV-dashboard";
        RootController rootController = stageManager.loadNodesView(
                ViewType.MAIN,
                controllerFactory
        );
        stageManager.showStage(stageTitle, rootController.getView());
    }

    /**
     * Handle the error when the user is not authenticated
     */
    private void handleError() {
        progressLoader.setVisible(false);
        loadingPane.setVisible(false);
        loadingPane.setStyle(CustomColor.TRANSPARENT.getStyle());
        errorPane.setVisible(true);
        AnimationUtil.animateInOut(errorPane,4, CustomColor.ERROR);
    }
}