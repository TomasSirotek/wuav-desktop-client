package com.wuav.client.gui.controllers;

import com.wuav.client.gui.controllers.abstractController.RootController;
import com.google.inject.Inject;


import eu.hansolo.fx.charts.BarChart;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.*;



public class DashboardController extends RootController implements Initializable {


    @FXML
    private HBox graphHbox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Platform.runLater(this::setupChart);
    }

    private void setupChart() {
        BarChart barChart = new BarChart();

     //   HeatMap heatMap = new HeatMap();
        // Set the dimensions of the HeatMap
//        heatMap.setSize(400, 300);
//
//        // Set the data values for the HeatMap
//        double[][] data = {
//                {0.1, 0.2, 0.3},
//                {0.4, 0.5, 0.6},
//                {0.7, 0.8, 0.9}
//        };
//        heatMap.addSpot(0, 0);

    }

    @Inject
    public DashboardController() {


    }











}
