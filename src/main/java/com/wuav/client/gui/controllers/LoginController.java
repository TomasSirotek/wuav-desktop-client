package com.wuav.client.gui.controllers;

import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.bll.services.interfaces.IAuthService;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.ControllerFactory;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import com.google.inject.Inject;
import com.wuav.client.gui.manager.StageManager;
import io.github.palexdev.materialfx.controls.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;


import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;


import javax.naming.AuthenticationException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginController extends RootController implements Initializable {
    @FXML
    private Pane errorPane,loadingPane;
    @FXML
    private MFXProgressSpinner progressLoader;
    @FXML
    private MFXPasswordField userPswField;

    @FXML
    private MFXTextField userEmailField;

    private final IControllerFactory controllerFactory;

    private final IAuthService authService;

    private final StageManager stageManager;

    ExecutorService executorService = Executors.newSingleThreadExecutor();
    @Inject
    public LoginController(IControllerFactory controllerFactory, IAuthService authService, StageManager stageManager) {
        this.controllerFactory = controllerFactory;
        this.authService = authService;
        this.stageManager = stageManager;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void login() {
        // Show the progress bar while the application is loading
        progressLoader.setVisible(true);
        loadingPane.setVisible(true);
        loadingPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2);");

        executorService.submit(() -> {
            try {
                // Authenticate the user and check authorization
                authService.authenticate(userEmailField.getText(), userPswField.getText());

                // Update the UI on the JavaFX application thread
                Platform.runLater(() -> {
                    try {
                        // Hide the progress bar
                        progressLoader.setVisible(false);
                        loadingPane.setStyle("-fx-background-color: transparent");

                        getStage().close();
                        RootController rootController = stageManager.loadNodesView(
                                ViewType.MAIN,
                                controllerFactory
                        );
                        stageManager.showStage("New Stage", rootController.getView());
                        executorService.shutdown();
                    } catch (IOException e) {
                        errorPane.setVisible(true);
                    }
                });
            } catch (AuthenticationException e) {
                // Update the UI on the JavaFX application thread
                Platform.runLater(() -> {
                    // Hide the progress bar
                    progressLoader.setVisible(false);
                    loadingPane.setVisible(false);
                    loadingPane.setStyle("-fx-background-color: transparent");
                    errorPane.setVisible(true);
                });
            }
        });
    }
}