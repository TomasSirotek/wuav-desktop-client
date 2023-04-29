package com.wuav.client.gui.controllers;

import com.wuav.client.be.Project;
import com.wuav.client.gui.controllers.abstractController.RootController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class ProjectController extends RootController implements Initializable {


    @FXML
    private TableColumn<Project,Button> colEdit;
    @FXML
    private TableView<Project> projectTable;
    @FXML
    private TableColumn<Project, CheckBox> colSelectAll;
    @FXML
    private TableColumn<Project,String> colDate;
    @FXML
    private TableColumn<Project,String> colName;
    @FXML
    private TableColumn<Project,Button> colStatus;
    @FXML
    private TableColumn<Project,String> colDes;
    @FXML
    private TableColumn<Project,String> colCustomer;
    @FXML
    private TableColumn<Project,String> colType;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillTable();

    }


    private void setTableWithProjects() {
        ObservableList<Project> projects = FXCollections.observableArrayList();

        Project project = new Project();
        project.setName("Project 1");
        project.setDescription("Description 1");
        project.setCustomerEmail("technician@hotmail.com");
        project.setType("Private");
        project.setCreatedAt(new Date("2021/01/01"));


        projects.add(project);



        projectTable.setItems(projects);
    }

    private void fillTable() {
        colSelectAll.setCellValueFactory(col -> {

            CheckBox checkBox = new CheckBox();
            checkBox.getStyleClass().add("checked-box");
            // set to be in the center of the cell
            checkBox.setAlignment(Pos.CENTER);
            checkBox.setOnAction(e -> {
                System.out.println("Selected: " + col.getValue());
            });
            return new SimpleObjectProperty<>(checkBox);
        });

        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        // status
        colStatus.setCellValueFactory(cellData -> {
           // create button with image and text
            MFXButton successInActiveButton = new MFXButton("Success");
            successInActiveButton.getStyleClass().add("success");
            // set button to be smaller with padding all around
          //  successInActiveButton.setMinSize(100, 20);

            successInActiveButton.setRippleAnimateBackground(false);
            successInActiveButton.setRippleBackgroundOpacity(0);
            successInActiveButton.setRippleRadius(0);
            successInActiveButton.setPrefWidth(100);
            successInActiveButton.setPrefHeight(20);

            // successInActiveButton.setPadding(new Insets(5, 5, 5, 5));
            successInActiveButton.setOnAction(e -> {
                System.out.println("Selected: " + cellData.getValue());
            });
            // set image to a button
            var imageIcon = new ImageView(new Image(getClass().getResourceAsStream("/openExpand.png")));
            imageIcon.setFitHeight(15);
            imageIcon.setFitWidth(15);
            successInActiveButton.setGraphic(imageIcon);

            return new SimpleObjectProperty<>(successInActiveButton);
        });

        // description
        colDes.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        // Customer
        colCustomer.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerEmail()));

        // Type
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));


        // Date
        colDate.setCellValueFactory(cellData -> {
            Date date = cellData.getValue().getCreatedAt();
            return new SimpleStringProperty(date == null ? "No data" : date.toString());
        });

        colEdit.setCellValueFactory(col -> {
            MFXButton playButton = new MFXButton("");
          //  playButton.getStyleClass().add("success");
            playButton.setPrefWidth(100);
            playButton.setPrefHeight(20);
            var imageIcon = new ImageView(new Image(getClass().getResourceAsStream("/edit.png")));
            imageIcon.setFitHeight(15);
            imageIcon.setFitWidth(15);
            playButton.setGraphic(imageIcon);
            playButton.setOnAction(e -> {
                System.out.println("Selected: " + col.getValue());
            });
            return new SimpleObjectProperty<>(playButton);
        });

        setTableWithProjects();

    }


}
