package com.wuav.client.gui.controllers;


import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import com.wuav.client.bll.helpers.EventType;
import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.bll.strategies.interfaces.IUserRoleStrategy;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import com.wuav.client.gui.controllers.event.RefreshEvent;
import com.wuav.client.gui.manager.StageManager;
import com.wuav.client.gui.models.user.CurrentUser;
import com.wuav.client.gui.utils.enums.CustomColor;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;


/**
 * The class BaseController.
 */
public class BaseController extends RootController implements Initializable {

    @FXML
    private MFXButton accButton, usersButton, projectButton, expand, dashboardButton;
    @FXML
    private VBox userDetailsBox, sideNavBox, expandBoxToggle;
    @FXML
    private ImageView userImage;
    @FXML
    private GridPane logoPane;
    @FXML
    private Label userNameField, userEmailField, menuItemLabel, wuavLogo;
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

    private final int SLIDER_CLOSED = 75;
    private final int SLIDER_OPEN = 230;

    /**
     * Instantiates a new Base controller.
     *
     * @param controllerFactory the controller factory
     * @param stageManager      the stage manager
     * @param eventBus          the event bus
     */
    @Inject
    public BaseController(IControllerFactory controllerFactory, StageManager stageManager, EventBus eventBus) {
        this.controllerFactory = controllerFactory;
        this.stageManager = stageManager;
        this.eventBus = eventBus;
    }

    /**
     * Initialize the controller
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventBus.register(this);
        setupButtons();
        setupStrategy();
        handleExpandControl();
        runInParallel(ViewType.DASHBOARD);
    }

    private void setupButtons() {
        expand.setStyle(CustomColor.TRANSPARENT.getStyle());
        dashboardButton.setStyle(CustomColor.HIGHLIGHTED.getStyle());
    }

    private void setupStrategy() {
        IUserRoleStrategy userRoleStrategy = CurrentUser.getInstance().getUserRoleStrategy();
        projectButton.setText(userRoleStrategy.getProjectButtonText());
        userImage.setImage(userRoleStrategy.getDefaultImage());
        usersButton.setVisible(userRoleStrategy.isUsersButtonVisible());
    }


    private void hideSidebar() {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(slider);

        menuItemLabel.setVisible(false);
        wuavLogo.setVisible(false);
        userDetailsBox.setVisible(false);
        expand.setPadding(new Insets(-10, -22, -10, 12));
        slide.setToX(0);
        slide.play();
        app_content.setStyle(CustomColor.NONE.getStyle());
        slider.setPrefWidth(SLIDER_CLOSED);
        userNameField.setText("");
        userEmailField.setText("");
        sideNavBox.getChildren().forEach(node -> {
            if (node instanceof Label) {
                ((Label) node).setStyle(CustomColor.TRANSPARENT_TEXT_FILL.getStyle());
            }
        });
    }

    private void showSidebar() {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(slider);
        userDetailsBox.setVisible(true);
        menuItemLabel.setVisible(true);
        wuavLogo.setVisible(true);
        expand.setPadding(new Insets(-10, -22, -10, -10));
        slide.play();

        slider.setPrefWidth(SLIDER_OPEN);
        userNameField.setText(CurrentUser.getInstance().getLoggedUser().getName());
        userEmailField.setText(CurrentUser.getInstance().getLoggedUser().getEmail());
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
    private void handleDashboardSwitch() {
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
    private void handleProjectPageSwitch() {
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
            Platform.runLater(() -> {
                switchToView(parent[0].getView());
            });
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
    private void logoutButton() throws IOException {
        CurrentUser.getInstance().logout();
        getStage().close();
        RootController rootController = stageManager.loadNodesView(ViewType.LOGIN, controllerFactory);
        stageManager.showStage("Login", rootController.getView());
    }
}
