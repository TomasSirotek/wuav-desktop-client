package com.wuav.client.gui.controllers;

import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.bll.services.interfaces.IAuthService;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import com.google.inject.Inject;
import com.wuav.client.gui.manager.StageManager;
import io.github.palexdev.materialfx.controls.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import animatefx.animation.*;

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

    private void loadNewView() throws IOException {
        RootController rootController = stageManager.loadNodesView(
                ViewType.MAIN,
                controllerFactory
        );
        stageManager.showStage("New Stage", rootController.getView());
    }

    private void handleError() {
        progressLoader.setVisible(false);
        loadingPane.setVisible(false);
        loadingPane.setStyle("-fx-background-color: transparent");
        errorPane.setVisible(true);
        FadeInDown fadeInDown = new FadeInDown(errorPane);
        fadeInDown.setOnFinished(event -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(4));
            pause.setOnFinished(event2 -> {
                FadeOutUp fadeOutDown = new FadeOutUp(errorPane);
                fadeOutDown.setOnFinished(event3 -> errorPane.setVisible(false));
                fadeOutDown.play();
            });
            pause.play();
        });
        fadeInDown.play();
    }
}