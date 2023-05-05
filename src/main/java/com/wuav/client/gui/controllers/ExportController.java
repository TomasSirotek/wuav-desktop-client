package com.wuav.client.gui.controllers;

import com.google.inject.Inject;
import com.wuav.client.be.Project;
import com.wuav.client.bll.utilities.AlertHelper;
import com.wuav.client.bll.utilities.pdf.IPdfGenerator;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.models.user.CurrentUser;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    private final IPdfGenerator pdfGenerator;

    @Inject
    public ExportController(IPdfGenerator pdfGenerator) {
        this.pdfGenerator = pdfGenerator;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exportActionBtn.setOnAction(e -> exportDocument());
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

    private void exportDocument() {
        if (formatSelect.getValue() != null) {
            String format = formatSelect.getValue().toString();
            switch (format) {
                case "PDF":
                    exportAsPDF();
                default:
                    break;
            }
    }
    }

    private void exportAsPDF() {
        ByteArrayOutputStream stream = pdfGenerator.generatePdf(CurrentUser.getInstance().getLoggedUser(), projectsToExport.get(0), "project");

        // Create a FileChooser instance
        FileChooser fileChooser = new FileChooser();

        // Set the title for the save dialog
        fileChooser.setTitle("Save PDF");

        // Set the initial file name
        fileChooser.setInitialFileName("project.pdf");

        // Set the file type filter to show only PDF files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show the save dialog and get the selected file
        File file = fileChooser.showSaveDialog(exportAnchorPane.getScene().getWindow());

        // If the user has selected a file
        if (file != null) {
            try {
                // Write the ByteArrayOutputStream to the selected file
                try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                    stream.writeTo(fileOutputStream);
                    AlertHelper.showDefaultAlert("Success !" , Alert.AlertType.CONFIRMATION);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


}
