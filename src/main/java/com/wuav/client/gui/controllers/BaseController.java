package com.wuav.client.gui.controllers;


import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.helpers.EventType;
import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.bll.strategies.interfaces.IUserRoleStrategy;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import com.wuav.client.gui.controllers.event.RefreshEvent;
import com.wuav.client.gui.manager.StageManager;
import com.wuav.client.gui.models.user.CurrentUser;
import com.wuav.client.gui.utils.AnimationUtil;
import com.wuav.client.gui.utils.enums.CustomColor;
import com.wuav.client.gui.utils.event.CustomEvent;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;


public class BaseController extends RootController implements Initializable {

    @FXML
    private MFXButton accButton,usersButton,projectButton,expand,dashboardButton;
    @FXML
    private VBox userDetailsBox,sideNavBox,expandBoxToggle;
    @FXML
    private ImageView userImage;
    @FXML
    private GridPane logoPane;
    @FXML
    private Label userNameField,userEmailField,menuItemLabel;
    @FXML
    private ImageView menuIcon;
    @FXML
    private StackPane app_content;
    @FXML
    private AnchorPane slider;

    private final IControllerFactory controllerFactory;

    private final StageManager stageManager;

    private final EventBus eventBus;

    private boolean isSidebarExpanded = false;

    private Image defaultImage = new Image("/no_data.png"); // has to be replaced

    @Inject
    public BaseController(IControllerFactory controllerFactory, StageManager stageManager, EventBus eventBus) {
        this.controllerFactory = controllerFactory;
        this.stageManager = stageManager;
        this.eventBus = eventBus;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventBus.register(this);
        expand.setStyle("-fx-text-fill: transparent;");
        projectButton.setStyle("-fx-background-color: rgba(234, 234, 234, 0.8);");



        if (!CurrentUser.getInstance().getLoggedUser().getRoles().get(0).getName().equals("TECHNICIAN")) {
            projectButton.setText("Projects");
            userImage.setImage(defaultImage);
            System.out.println(CurrentUser.getInstance().getLoggedUser().getRoles().get(0));

            if (CurrentUser.getInstance().getLoggedUser().getRoles().get(0).getName().equals("ADMIN")) {
                usersButton.setVisible(true);
            } else {
                usersButton.setVisible(false);
            }
        } else {
            usersButton.setVisible(false);
        }


        handleExpandControl();
        runInParallel(ViewType.DASHBOARD);

    }


    private void hideSidebar() {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(slider);
        menuItemLabel.setVisible(false);

        userDetailsBox.setVisible(false);
        expandBoxToggle.setAlignment(Pos.CENTER_LEFT);
        logoPane.setVisible(true);
        slide.setToX(0);
        slide.play();

        Image image = new Image(getClass().getClassLoader().getResource("close.png").toExternalForm());
        app_content.setStyle("-fx-background-color: none;");
        menuIcon.setImage(image);

        slider.setPrefWidth(80);
        userNameField.setText("");
        userEmailField.setText("");
        sideNavBox.getChildren().forEach(node -> {
            if (node instanceof Label) {
                ((Label) node).setStyle("-fx-text-fill: transparent;");
            }
        });

        slide.setOnFinished(ActionEvent -> {

        });
    }

    private void showSidebar() {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(slider);
        userDetailsBox.setVisible(true);
        menuItemLabel.setVisible(true);
        expandBoxToggle.setAlignment(Pos.CENTER_RIGHT);
        logoPane.setVisible(false);

        Image image = new Image(getClass().getClassLoader().getResource("close.png").toExternalForm());
        menuIcon.setImage(image);
        slide.play();

        slider.setPrefWidth(250); // Replace with your original sidebar width
        userNameField.setText(CurrentUser.getInstance().getLoggedUser().getName()); // Replace with your original text
        userEmailField.setText(CurrentUser.getInstance().getLoggedUser().getEmail()); // Replace with your original text
        sideNavBox.getChildren().forEach(node -> {
            if (node instanceof Label) {
                ((Label) node).setStyle("-fx-text-fill: black;");
            }
        });
    }

    /**
     * This method is used for handling expand control
     */
    private void handleExpandControl() {
        expand.setOnAction(event -> {
            if (isSidebarExpanded) {
                isSidebarExpanded = false;
                hideSidebar();
            } else {
                isSidebarExpanded = true;
                showSidebar();
            }
        });
    }

    @FXML
    public void handleDashboardSwitch() {
        dashboardButton.setStyle(CustomColor.HIGHLIGHTED.getStyle());
        projectButton.setStyle(CustomColor.TRANSPARENT.getStyle());
        usersButton.setStyle(CustomColor.TRANSPARENT.getStyle());
        accButton.setStyle(CustomColor.TRANSPARENT.getStyle());
        runInParallel(ViewType.DASHBOARD);
    }
    @FXML
    private void handleAllUsersSwitch() {
        projectButton.setStyle(CustomColor.TRANSPARENT.getStyle());
        dashboardButton.setStyle(CustomColor.TRANSPARENT.getStyle());
        accButton.setStyle(CustomColor.TRANSPARENT.getStyle());
        usersButton.setStyle(CustomColor.HIGHLIGHTED.getStyle());
        runInParallel(ViewType.ALL_USERS);
    }

    @FXML
    private void handleDashBoardPageSwitch() {
        projectButton.setStyle(CustomColor.HIGHLIGHTED.getStyle());
        dashboardButton.setStyle(CustomColor.TRANSPARENT.getStyle());
        usersButton.setStyle(CustomColor.TRANSPARENT.getStyle());
        accButton.setStyle(CustomColor.TRANSPARENT.getStyle());
        eventBus.post(new RefreshEvent(EventType.UPDATE_TABLE));
        runInParallel(ViewType.PROJECTS);
    }

    @FXML
    private void handleUserProfileSwitch() {
        dashboardButton.setStyle(CustomColor.TRANSPARENT.getStyle());
        projectButton.setStyle(CustomColor.TRANSPARENT.getStyle());
        usersButton.setStyle(CustomColor.TRANSPARENT.getStyle());
        accButton.setStyle(CustomColor.HIGHLIGHTED.getStyle());
        runInParallel(ViewType.USER_PROFILE);
    }
    //endregion

    private void runInParallel(ViewType type) {
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


    private RootController loadNodesView(ViewType viewType) throws IOException {
        return controllerFactory.loadFxmlFile(viewType);
    }

    private void switchToView(Parent parent) {
        app_content.getChildren().clear();
        app_content.getChildren().add(parent);
    }

    @FXML
    public void logoutButton(ActionEvent actionEvent) throws IOException {
        CurrentUser.getInstance().logout();

        getStage().close();
        RootController rootController = stageManager.loadNodesView(ViewType.LOGIN,controllerFactory);
        stageManager.showStage("Login",rootController.getView());
    }


}
