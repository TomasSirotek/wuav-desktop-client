package com.wuav.client.gui.controllers;

import com.wuav.client.bll.strategies.interfaces.IUserRoleStrategy;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.google.inject.Inject;


import com.wuav.client.gui.entities.DashboardData;
import com.wuav.client.gui.models.user.CurrentUser;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;


import java.net.URL;
import java.util.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.statistics.HistogramDataset;

import javafx.scene.layout.GridPane;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYBarDataset;

public class DashboardController extends RootController implements Initializable {

    @FXML
    public Label plansCount;
    @FXML
    private Label deviceCount;
    @FXML
    private Label totalProjectCount;


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
        data.recentCustomers().forEach(System.out::println);
    }

    @Inject
    public DashboardController() {

    }





}
