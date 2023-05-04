package com.wuav.client.gui.controllers;

import com.wuav.client.be.Project;
import com.wuav.client.gui.controllers.abstractController.RootController;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ExportController extends RootController implements Initializable {


    @FXML
    private ListView selectedProjectsForExport;

    @FXML
    private ChoiceBox formatSelect;

    @FXML
    private MFXButton exportActionBtn;
    @FXML
    private AnchorPane exportAnchorPane;

    private List<Project> projectsToExport = new ArrayList<Project>();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            Stage stage = (Stage) exportAnchorPane.getScene().getWindow();
            projectsToExport = (List<Project>) stage.getProperties().get("projectsToExport");
            if (projectsToExport != null) {
                ObservableList<Project> selectedProjects = FXCollections.observableArrayList(projectsToExport);
                selectedProjectsForExport.setItems(selectedProjects);
                formatSelect.getItems().add("PDF");
            }
        });
    }
}
