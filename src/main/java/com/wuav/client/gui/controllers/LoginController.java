package com.wuav.client.gui.controllers;

import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.bll.services.interfaces.IAuthService;
import com.wuav.client.bll.utilities.email.EmailConnectionFactory;
import com.wuav.client.bll.utilities.email.IEmailSender;
import com.wuav.client.bll.utilities.engines.IEmailEngine;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import com.google.inject.Inject;
import com.wuav.client.gui.models.user.IUserModel;
import com.wuav.client.gui.utils.AlertHelper;
import io.github.palexdev.materialfx.controls.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;


import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


import javax.mail.Session;
import javax.naming.AuthenticationException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class LoginController extends RootController implements Initializable {


    @FXML
    private Pane loadingPane;
    @FXML
    private MFXProgressSpinner progressLoader;
    @FXML
    private MFXPasswordField userPswField;

    @FXML
    private MFXTextField userEmailField;
    @FXML
    private MFXButton login;

    @FXML
    private StackPane baseContent;
    private final IControllerFactory controllerFactory;

    private final IAuthService authService;

    @Inject
    public LoginController(IControllerFactory controllerFactory, IAuthService authService) {
        this.controllerFactory = controllerFactory;
        this.authService = authService;

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    private RootController loadNodesView(ViewType viewType) throws IOException {
        return controllerFactory.loadFxmlFile(viewType);
    }


    @FXML
    private void login() {
        // Show the progress bar while the application is loading
        progressLoader.setVisible(true);
        loadingPane.setVisible(true);
        loadingPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2);");
        // Use a new thread to authenticate the user and check authorization
        new Thread(() -> {
            try {
                // Authenticate the user and check authorization
                AppUser authenticatedUser = authService.authenticate(userEmailField.getText(), userPswField.getText());
                boolean isAuthorized = authService.isAuthorized(authenticatedUser);

                // Update the UI on the JavaFX application thread
                Platform.runLater(() -> {
                    // Hide the progress bar
                    progressLoader.setVisible(false);
                    loadingPane.setStyle("-fx-background-color: transparent");

                    // Show the logged view if the user is authorized
                    if (isAuthorized) {
                        var test = tryToLoadView();
                        getStage().close();
                        show(test.getView(), "Logged view ");
                    }
                });
            } catch (AuthenticationException e) {
                // Handle authentication failure
                Platform.runLater(() -> {
                    // Hide the progress bar
                    progressLoader.setVisible(false);
                    progressLoader.setVisible(false);
                    loadingPane.setVisible(false);
                    // Show an error message
                    AlertHelper.showDefaultAlert("Authentication failed " + e.getMessage(), Alert.AlertType.ERROR);
                });
            }
        }).start();
    }

    /**
     * private method for showing new stages whenever its need
     *
     * @param parent root that will be set
     * @param title  title for new stage
     */
    private void show(Parent parent, String title) {
        Stage stage = new Stage();
        Scene scene = new Scene(parent);

        stage.initOwner(getStage());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    private RootController tryToLoadView() {
        try {
            return loadNodesView(ViewType.MAIN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
