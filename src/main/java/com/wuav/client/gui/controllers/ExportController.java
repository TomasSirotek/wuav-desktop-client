package com.wuav.client.gui.controllers;

import com.google.inject.Inject;
import com.wuav.client.be.Project;
import com.wuav.client.bll.helpers.ViewType;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import com.wuav.client.gui.manager.StageManager;
import com.wuav.client.gui.models.IProjectModel;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The class ExportController.
 */
public class ExportController extends RootController implements Initializable {


    @FXML
    private MFXScrollPane exportPane;
    @FXML
    private MFXButton exportActionBtn;
    @FXML
    private AnchorPane exportAnchorPane;

    private List<Project> projectsToExport = new ArrayList<Project>();


    private IProjectModel projectModel;

    private Image defaultImage = new Image("/pdf.png");

    private final IControllerFactory controllerFactory;

    private final StageManager stageManager;

    /**
     * Instantiates a new Export controller.
     *
     * @param projectModel      the project model
     * @param controllerFactory the controller factory
     * @param stageManager      the stage manager
     */
    @Inject
    public ExportController(IProjectModel projectModel, IControllerFactory controllerFactory, StageManager stageManager) {
        this.projectModel = projectModel;
        this.controllerFactory = controllerFactory;
        this.stageManager = stageManager;
    }

    /**
     * Initialize.
     *
     * @param url            the url
     * @param resourceBundle the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exportActionBtn.setOnAction(e -> exportDocument());
        setupProjectsToExport();
    }

    private void setupProjectsToExport() {
        Platform.runLater(() -> {
            Stage stage = (Stage) exportAnchorPane.getScene().getWindow();
            projectsToExport = (List<Project>) stage.getProperties().get("projectsToExport");
            if (projectsToExport != null) {
                ObservableList<Project> selectedProjects = FXCollections.observableArrayList(projectsToExport);
                constructScrollPane(selectedProjects);
            }
        });
    }

    private void constructScrollPane(ObservableList<Project> selectedProjects) {

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10); // Set horizontal gap between grid cells
        gridPane.setVgap(10); // Set vertical gap between grid cells
        // Iterate over the selected projects
        for (int i = 0; i < selectedProjects.size(); i++) {
            Project project = selectedProjects.get(i);
            // Create an ImageView
            ImageView imageView = new ImageView(defaultImage); // Replace `project.getImage()` with your image source
            imageView.setFitWidth(30); // Set the desired width for the ImageView
            imageView.setFitHeight(30); // Set the desired height for the ImageView

            // Create a Label for export name
            Label exportNameLabel = new Label("Export Name: "); // Replace with your export name

            // Create a Label for project name
            Label projectNameLabel = new Label(project.getName());
            projectNameLabel.setStyle("-fx-font-weight: bold; -fx-font-family: 'Arial';");

            // Add the nodes to the grid pane
            gridPane.add(imageView, 0, i); // ImageView in the first column
            gridPane.add(exportNameLabel, 1, i); // Export Name Label in the second column
            gridPane.add(projectNameLabel, 2, i); // Project Name Label in the third column
        }

        // Set the content of the MFXScrollPane to the GridPane
        exportPane.setContent(gridPane);
    }

    private void exportDocument() {
        openBuilderView();
    }

    private void openBuilderView() {
        RootController controller = tryToLoadView(ViewType.PDF_BUILDER);
        Stage stage = new Stage();
        Scene scene = new Scene(controller.getView());

        stage.initOwner(getStage());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Build your PDF");
        stage.setOnCloseRequest(e -> {

        });
        // set on showing event to know about the previous stage so that it can be accessed from modalAciton controlelr
        stage.setOnShowing(e -> {
            stage.getProperties().put("projectToExport", projectsToExport.get(0));
        });
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    }

    private RootController loadNodesView(ViewType viewType) throws IOException {
        return controllerFactory.loadFxmlFile(viewType);
    }

    private RootController tryToLoadView(ViewType viewType) {
        try {
            return loadNodesView(viewType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
