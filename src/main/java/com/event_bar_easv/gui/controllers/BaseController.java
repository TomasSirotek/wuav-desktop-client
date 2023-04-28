package com.event_bar_easv.gui.controllers;


import com.event_bar_easv.gui.models.CurrentUser;
import com.google.inject.Inject;
import com.event_bar_easv.bll.helpers.ViewType;
import com.event_bar_easv.gui.controllers.abstractController.RootController;
import com.event_bar_easv.gui.controllers.controllerFactory.IControllerFactory;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.*;


public class BaseController extends RootController implements Initializable {

    @FXML
    private Button specialTicketButton;

    @FXML
    private Button usersTicketButton;
    @FXML
    private Label userEmail;

    @FXML
    private Label userRole;
    @FXML
    private ScrollPane scroll_pane;
    @FXML
    private StackPane app_content;

    private final IControllerFactory controllerFactory;

    @Inject
    public BaseController(IControllerFactory controllerFactory) {
        this.controllerFactory = controllerFactory;

    }

    private void tempAuth() {

        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Authorize yourself");
        stage.initStyle(StageStyle.DECORATED);
        stage.setResizable(false);

        Label usernameLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setText("admin@yahoo.com");
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button submitButton = new Button("Submit");
        HBox buttonContainer = new HBox(submitButton);
        buttonContainer.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10, usernameLabel, emailField, passwordLabel, passwordField, buttonContainer);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout);
        stage.setScene(scene);

        submitButton.setOnAction(e -> {
            String email = emailField.getText();

            CurrentUser currentUser = CurrentUser.getInstance();
            currentUser.login(email);

            if(currentUser.getLoggedUser().getEmail().equals(email)){
                userEmail.setText(currentUser.getLoggedUser().getEmail());
                userRole.setText(currentUser.getLoggedUser().getRoles().get(0).getName());

                if(currentUser.getLoggedUser().getRoles().get(0).getName().equals("coordinator")){
                    specialTicketButton.setDisable(true);
                    usersTicketButton.setDisable(true);
                }

                stage.close();
                handleDashBoardPageSwitch();
            }
            else {
                System.out.println("Wrong credentials");
            }


        });

        stage.showAndWait();


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tempAuth();

    }

    //region PAGE SWITCHING EVENTS
    @FXML
    private void handleEventPageSwitch()  {
        runInParallel(ViewType.EVENTS);
    }

    @FXML
    private void handleUsersPageSwitch()  {
        runInParallel(ViewType.USERS);
    }

    @FXML
    private void handleSpecialTicketsPageSwitch()  {
        runInParallel(ViewType.SPECIAL_TICKETS);
    }

    @FXML
    private void handleDashBoardPageSwitch()  {
        runInParallel(ViewType.DASHBOARD);
    }
    //endregion

    private void runInParallel(ViewType type){
        final RootController[] parent = {null};
        Task<Void> loadDataTask = new Task<>() {
            @Override
            protected Void call() throws IOException {
                parent[0] = loadNodesView(type);
                return null;
            }
        };
        loadDataTask.setOnSucceeded(event -> {
            switchToView(parent[0].getView());
        });
        new Thread(loadDataTask).start();
    }



    //region VIEW INTERNAL HANDEL

    private RootController tryToLoadView() {
        try {
            return loadNodesView(ViewType.EVENTS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        stage.initStyle(StageStyle.UNDECORATED);

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    private RootController loadNodesView(ViewType viewType) throws IOException {
        return controllerFactory.loadFxmlFile(viewType);
    }

    private void switchToView(Parent parent) {
        app_content.getChildren().clear();
        app_content.getChildren().add(parent);
    }
    //endregion


}
