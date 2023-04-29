package com.wuav.client.gui.controllers;

import com.wuav.client.gui.controllers.abstractController.RootController;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ProjectActionController  extends RootController implements Initializable {

    @FXML
    private HBox imageActionHandleBox;
    @FXML
    private ImageView selectedImage;
    @FXML
    private MFXButton selectFile;

    @FXML
    private HBox selectedFileHBox;

    private MFXButton removeImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectFile.setOnAction(e -> selectFile());
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

            selectedImage.setImage(new javafx.scene.image.Image(selectedFile.toURI().toString()));
            selectedFileHBox.setVisible(true);

            changeImageActionHandleBox();
        }

    }

    private void changeImageActionHandleBox() {
        imageActionHandleBox.getChildren().clear();
         removeImage = new MFXButton("Remove Image");
        removeImage.getStyleClass().add("mfx-raised");
        removeImage.setStyle("-fx-background-color: red; -fx-text-fill: #ffffff;");
        removeImage.setOnAction(e -> removeImage());
        imageActionHandleBox.getChildren().add(removeImage);
    }

    private void removeImage() {
        selectedImage.setImage(null);
        // set image back to the defualt not data selected no data in resource folder
        selectedImage.setImage(new javafx.scene.image.Image("/no_data.png"));
        // remove action button and set label back to no image uploaded

        imageActionHandleBox.getChildren().clear();
        Label noImageUploaded = new Label("No Image Uploaded");
        imageActionHandleBox.getChildren().add(noImageUploaded);
       //  selectedFileHBox.setVisible(false);
    }
}
