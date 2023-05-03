package com.wuav.client.gui.controllers;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.wuav.client.be.Project;
import com.wuav.client.bll.helpers.EventType;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import com.wuav.client.gui.models.IProjectModel;
import com.wuav.client.gui.models.user.CurrentUser;
import com.wuav.client.gui.utils.AlertHelper;
import com.wuav.client.gui.utils.ProjectEvent;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.web.WebEngine;



public class ProjectActionController  extends RootController implements Initializable {

    @FXML
    private TabPane tabPane;
    @FXML
    private MFXTextField descriptionField;
    @FXML
    private MFXButton continueBtn;
    @FXML
    private Label projectNameField;
    @FXML
    private Tab clientTab;
    @FXML
    private VBox mapVBox;
    @FXML
    private HBox imageActionHandleBox;
    @FXML
    private ImageView selectedImage;
    @FXML
    private MFXButton selectFile;

    @FXML
    private HBox selectedFileHBox;

    private MFXButton removeImage;

    private final IControllerFactory controllerFactory;


    private final IProjectModel projectModel;

    private Project currentProject;



    private Image defaultImage = new Image("/no_data.png");

    private File selectedImageFile;


    @Inject
    public ProjectActionController(IControllerFactory controllerFactory, IProjectModel projectModel) {
        this.controllerFactory = controllerFactory;
        this.projectModel = projectModel;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedImage.setImage(defaultImage);
        selectFile.setOnAction(e -> selectFile());
      //  saveImageDesc.setOnAction(e -> saveImageDesc());

        tabPane.getTabs().get(1).setDisable(true);
        tabPane.getTabs().get(2).setDisable(true);
        continueBtn.setOnAction(e -> {
            tabPane.getTabs().get(0).setDisable(true);
            tabPane.getTabs().get(1).setDisable(false);
            tabPane.getSelectionModel().selectNext();
        });

      //  eventBus.register(this);
      //  projectNameField.setText(currentProject.getName());
      //  projectNameField.setText(currentProject.getName());
        // execute this code when tab is switched to clientTab
        clientTab.setOnSelectionChanged(e -> {
            if(clientTab.isSelected()) {
              //  loadMap();

            }
        });
    }

    private void saveImageDesc() {

        if(selectedImage != null && !selectedImage.getImage().equals(defaultImage) && !descriptionField.getText().trim().isEmpty()) {
            System.out.println("Saving image and description" + selectedImage.getImage());
            System.out.println("Description: " + descriptionField.getText().trim());

            // user id
            int userId = CurrentUser.getInstance().getLoggedUser().getId();
            // project id
            int projectId = currentProject.getId();
            // create file from image url
            File imageFile = new File(selectedImageFile.toURI());
            // image description
            String imageDescription = descriptionField.getText().trim();
            // is main image
            boolean isMainImage = true;
            // send image and description to the services with userId and project id to and if its main image or not
            var uploadedStatus = projectModel.uploadImageWithDescription(userId, projectId, imageFile, imageDescription, isMainImage);
            System.out.println("Uploaded status: " + uploadedStatus);
            if(uploadedStatus) {
                System.out.println("Image uploaded successfully");
                // show success message
                AlertHelper.showDefaultAlert("Saved successfully", Alert.AlertType.CONFIRMATION);

                // move to another tab
            }

        } else {
            System.out.println("Selected image is equal to default image, not saving.");
        }

    }

    @Subscribe
    public void handleProjectSet(ProjectEvent event) {
       System.out.println("Handling project event: " + event.eventType());
       System.out.println("Project: " + event.getProject());
     //  currentProject = event.getProject();
     //  System.out.println("current project " + currentProject.toString());
     //  System.out.println("current project " + currentProject.getName());
       projectNameField.setText("etesffs");
    }

    @Subscribe
    public void handleCategoryEvent(ProjectEvent event) {
        if (event.eventType() == EventType.SET_CURRENT_PROJECT) {
            System.out.println("Handling project event: " + event.eventType());
        }
    }

    public void setCurrentProject(Project project) {
        System.out.println("Setting current project: " + project);
        currentProject = project;
        projectNameField.setText(currentProject.getName());
    }

    private void loadMap() {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(getClass().getResource("/googleMap.html").toString());
        mapVBox.getChildren().add(webView);

    }


    // show transaction erro however that is just apple not liking javaFx and its not a real error
    private void selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        // select only jpeg and png files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        // show open file dialog
        File selectedFile = fileChooser.showOpenDialog(getStage());
        if(selectedFile != null) {
            // set image fit to width and height
            selectedImage.setPreserveRatio(true);
            selectedImage.setFitHeight(600);
            selectedImage.setFitHeight(500);
            selectedImageFile = selectedFile;
            selectedImage.setImage(new javafx.scene.image.Image(selectedFile.toURI().toString()));
            selectedFileHBox.setVisible(true);
            selectFile.setDisable(true);
            changeImageActionHandleBox();
            changeSelectedFileHBox();


        }

    }

    private void changeSelectedFileHBox() {
        selectedFileHBox.getChildren().clear();
       // create hbox with label at the start and button at the end with x as text and make the box light red and so that text start at the start and ubtton at the end
        Label selectedFileLabel = new Label("Selected File: ");
        selectedFileHBox.setStyle("-fx-text-fill: #ffffff;");
        selectedFileHBox.setStyle("-fx-background-color: #E84910; -fx-spacing: 10; -fx-opacity: 0.8; -fx-padding: 10;");
        selectedFileHBox.getChildren().add(selectedFileLabel);
        Label selectedFileName = new Label("image.png");
        selectedFileName.setStyle("-fx-text-fill: black;");
        selectedFileHBox.getChildren().add(selectedFileName);
        MFXButton removeFile = new MFXButton("X");
        removeFile.getStyleClass().add("mfx-raised");
        removeFile.setStyle("-fx-background-color: red; -fx-text-fill: #ffffff;");
        removeFile.setOnAction(e -> removeImage());
        selectedFileHBox.getChildren().add(removeFile);

    }

    private void changeImageActionHandleBox() {
        imageActionHandleBox.getChildren().clear();
        // add new button preview that has png image inside
        MFXButton preview = new MFXButton("Preview");
        preview.getStyleClass().add("mfx-raised");
        preview.setStyle("-fx-background-color: #E84910; -fx-text-fill: #ffffff;");
        preview.setOnAction(e -> previewImage());
        imageActionHandleBox.getChildren().add(preview);

    }

    private void previewImage() {
        // open new scene with image inside
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Authorize yourself");
        stage.initStyle(StageStyle.DECORATED);
        stage.setResizable(false);

        ImageView imageView = new ImageView(selectedImage.getImage());
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(600);
        imageView.setFitHeight(500);

        VBox layout = new VBox(10, imageView);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout);
        stage.setScene(scene);


        stage.showAndWait();

    }

    private void removeImage() {
        selectedImage.setImage(null);
        // set image back to the defualt not data selected no data in resource folder
        selectedImage.setImage(defaultImage);
        // remove action button and set label back to no image uploaded

        imageActionHandleBox.getChildren().clear();
        Label noImageUploaded = new Label("No Image Uploaded");
        imageActionHandleBox.getChildren().add(noImageUploaded);
        // clean selected file hbox and set back the text to no file selected
        selectedFileHBox.getChildren().clear();
        Label noFileSelected = new Label("No File Selected");
        selectedFileHBox.getChildren().add(noFileSelected);
        // set back the color back to white
        selectedFileHBox.setStyle("-fx-background-color: #ffffff;");
        selectFile.setDisable(false);
    }
}
