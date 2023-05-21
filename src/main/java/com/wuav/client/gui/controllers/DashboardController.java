package com.wuav.client.gui.controllers;

import com.wuav.client.bll.strategies.interfaces.IUserRoleStrategy;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.google.inject.Inject;


import com.wuav.client.gui.entities.DashboardData;
import com.wuav.client.gui.models.user.CurrentUser;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.*;
import javafx.scene.layout.GridPane;

public class DashboardController extends RootController implements Initializable {

    @FXML
    public Label plansCount;
    @FXML
    private Label deviceCount;
    @FXML
    private Label totalProjectCount;
    @FXML
    private GridPane pane;

    @FXML
    private MFXButton dashboardToggle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupData();
    }

    private void setupData() {
        IUserRoleStrategy userRoleStrategy = CurrentUser.getInstance().getUserRoleStrategy();
        DashboardData data = userRoleStrategy.getDashboardData(CurrentUser.getInstance().getLoggedUser());
        totalProjectCount.setText(String.valueOf(data.totalProjects()));
        plansCount.setText(String.valueOf(data.amountOfPlansUploaded()));
        deviceCount.setText(String.valueOf(data.totalDeviceUser()));
        swapButtonsInNonTechnicianRole();
    }

    /**
     * This method is used swap the buttons when the user is not a technician in order with beautiful strategy pattern
     * to disallow the user to create projects
     */
    private void swapButtonsInNonTechnicianRole() {
        IUserRoleStrategy strategy = CurrentUser.getInstance().getUserRoleStrategy();
        dashboardToggle.setText(strategy.getDashboardButtonText());
    }
    @Inject
    public DashboardController() {
    }











}
