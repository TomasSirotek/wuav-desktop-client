package com.wuav.client.gui.controllers;

import com.wuav.client.gui.controllers.abstractController.RootController;
import com.google.inject.Inject;


import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.chart.CategoryAxis;
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
    private GridPane pane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(this::setupChart);
    }

    private void setupChart() {
        // Create a dataset with some random values
        HistogramDataset dataset = new HistogramDataset();
        double[] values = {1.2, 2.5, 3.8, 4.1, 5.3, 5.7, 6.2, 7.9, 8.5, 9.2};
        dataset.addSeries("Histogram", values, 10); // 10 bins

        // Create the chart
        JFreeChart chart = ChartFactory.createHistogram(null, null, null, dataset);

        // Create a SwingNode to host the chart panel
        SwingNode swingNode = new SwingNode();
        ChartPanel chartPanel = new ChartPanel(chart);
        swingNode.setContent(chartPanel);


        pane.getChildren().add(swingNode);


    }

    @Inject
    public DashboardController() {


    }











}
