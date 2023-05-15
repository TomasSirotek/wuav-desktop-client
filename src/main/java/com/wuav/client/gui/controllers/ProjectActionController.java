package com.wuav.client.gui.controllers;

import com.google.inject.Inject;
import com.wuav.client.be.CustomImage;
import com.wuav.client.be.Customer;
import com.wuav.client.be.Project;
import com.wuav.client.cache.ImageCache;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import com.wuav.client.gui.dto.PutAddressDTO;
import com.wuav.client.gui.dto.PutCustomerDTO;
import com.wuav.client.gui.models.IProjectModel;
import com.wuav.client.gui.utils.AlertHelper;
import com.wuav.client.gui.utils.CKEditorPane;
import com.wuav.client.gui.utils.validations.FormField;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;


public class ProjectActionController  extends RootController implements Initializable {


    @FXML
    private MFXButton updateClient;
    @FXML
    private MFXProgressSpinner statusSpinner;
    @FXML
    private MFXButton uploadBtn;
    @FXML
    private MFXButton cancelBtn;
    @FXML
    private Label fileName;
    @FXML
    private HBox newFileUploadBox;
    @FXML
    private VBox editorBox;
    @FXML
    private MFXButton expand1;

    @FXML
    private MFXButton expand2;

    @FXML
    private MFXButton expandBtn;
    @FXML
    private MFXButton updateBtnNotes;
    @FXML
    private ImageView firstUploadedImage;
    @FXML
    private ImageView secondUploadedImage;
    @FXML
    private MFXTextField clientNameField;
    @FXML
    private MFXTextField clientEmailField;
    @FXML
    private MFXScrollPane deviceForProjectList;
    @FXML
    private ChoiceBox clientTypeChooseField;
    @FXML
    private MFXTextField clientPhoneField;
    @FXML
    private MFXTextField clientCityField;
    @FXML
    private TextField clientAddress;
    @FXML
    private TabPane tabPane;

    @FXML
    private Label projectNameField;
    @FXML
    private Tab clientTab;
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

    private Image mainImage;

    private StringProperty editorContent = new SimpleStringProperty();

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
        expandBtn.setOnAction(e -> previewImage(selectedImage.getImage()));
        updateBtnNotes.setOnAction(e -> updateNotes());
        newFileUploadBox.setVisible(false);
        selectedImageFile = null;
        updateClient.setOnAction(e -> updateClient());
    }

    private void updateClient() {

      if(validateFields()) {
          PutAddressDTO addressDTO = new PutAddressDTO(
                  currentProject.getCustomer().getAddress().getId(),
                  clientAddress.getText(),
                  clientCityField.getText(),
                  clientPhoneField.getText()
          );

          PutCustomerDTO customerDTO = new PutCustomerDTO(
                  currentProject.getCustomer().getId(),
                  clientNameField.getText(),
                  clientEmailField.getText(),
                  clientPhoneField.getText(),
                  clientTypeChooseField.getSelectionModel().getSelectedItem().toString(),
                  addressDTO
          );


          Customer updatedCustomer = projectModel.updateCustomer(customerDTO);
          if (updatedCustomer != null) {
              AlertHelper.showDefaultAlert("Client updated successfully", Alert.AlertType.INFORMATION);
              currentProject.setCustomer(updatedCustomer);
          }
      }

    }

    private boolean validateFields() {
        boolean isValid = true;
        List<FormField> fieldsToValidate = Arrays.asList(
                new FormField(clientNameField, "Client name is required"),
                new FormField(clientEmailField, "Client email is required", this::isValidEmail, "Invalid email format"),
                new FormField(clientTypeChooseField, "Client type is required"),
                new FormField(clientPhoneField, "Client phone is required", this::isValidPhone, "Invalid phone number format"),
                new FormField(clientCityField, "Client city is required")
                );

        for (FormField field : fieldsToValidate) {
            if (field.getText().isEmpty()) {
                AlertHelper.showDefaultAlert(field.getErrorMessage(), Alert.AlertType.WARNING);
                isValid = false;
            } else if (field.getValidationFunction() != null && !field.getValidationFunction().validate(field.getText())) {
                AlertHelper.showDefaultAlert(field.getErrorValidationMessage(), Alert.AlertType.WARNING);
                isValid = false;
            }
        }

        if (clientTypeChooseField.getSelectionModel().isEmpty()) {
            AlertHelper.showDefaultAlert("Client type is required", Alert.AlertType.WARNING);
            isValid = false;
        }

        return isValid;

    }

    private boolean isValidEmail(String email) {
        // Implement email validation logic here
        return true;
    }

    private boolean isValidPhone(String phone) {
        // Implement phone number validation logic here
        return true;
    }

    private void updateNotes() {
        if(!editorContent.get().isEmpty()){
           String content = projectModel.updateNotes(currentProject.getId(), editorContent.get().trim());
           if(!content.isEmpty()){
               editorContent.set(content);
               AlertHelper.showDefaultAlert("Notes updated successfully", Alert.AlertType.INFORMATION);
              }else{
               AlertHelper.showDefaultAlert("Notes update failed", Alert.AlertType.ERROR);
           }
        }
    }

    private List<HBox> deviceDetailsList = new ArrayList<>();
    // if router here set all the info
    public void setCurrentProject(Project project) {
        currentProject = project;
        projectNameField.setText(currentProject.getName());

        CKEditorPane editorPane = new CKEditorPane();
        editorPane.setContent(currentProject.getDescription());

        editorPane.editorContentProperty().addListener((observable, oldValue, newValue) -> {
            editorContent.set(newValue);
        });

        editorBox.getChildren().add(editorPane);

        clientNameField.setText(currentProject.getCustomer().getName());
        clientEmailField.setText(currentProject.getCustomer().getEmail());

        ObservableList<String> options = FXCollections.observableArrayList("PRIVATE", "BUSINESS");
        clientTypeChooseField.setItems(options);
        clientTypeChooseField.setValue(currentProject.getCustomer().getType());

        clientPhoneField.setText(currentProject.getCustomer().getPhoneNumber());
        clientCityField.setText(currentProject.getCustomer().getAddress().getCity());

        clientAddress.setText(currentProject.getCustomer().getAddress().getStreet());


        project.getDevices().forEach(device -> {
            Label deviceTypeName = new Label(device.getName());
            deviceTypeName.setStyle("-fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-min-width: 100px; -fx-max-width: 100px;");

            Label deviceTypeLabel = new Label(device.getDeviceType().toLowerCase());
            deviceTypeLabel.setStyle("-fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-min-width: 80px; -fx-max-width: 80px;");

            Button editButton = new Button("Edit");
            editButton.setStyle("-fx-min-width: 82px; -fx-max-width: 82px;");
            editButton.setOnAction(event -> {
                // openDeviceWindow(true,selectedDevice);
                // maybe edit here
            });

            // Create an HBox for the device details
            HBox deviceDetails = new HBox(deviceTypeName, deviceTypeLabel,editButton);
            deviceDetails.setSpacing(10);
            deviceDetails.setStyle("-fx-min-height: 20px; -fx-alignment: CENTER_LEFT;");

            // Add the device details HBox to the list
            deviceDetailsList.add(deviceDetails);
        });



        VBox scrollPaneContent = new VBox();
        scrollPaneContent.setSpacing(10);
        scrollPaneContent.getChildren().addAll(deviceDetailsList);
        deviceForProjectList.setContent(scrollPaneContent);


        AtomicInteger nonMainImageCounter = new AtomicInteger(0);

        project.getProjectImages().forEach(image -> {
            if (image.isMainImage()) {
                selectedImage.setImage(ImageCache.getImage(image.getId()));
                mainImage = ImageCache.getImage(image.getId());
            } else {
                int nonMainImageIndex = nonMainImageCounter.getAndIncrement();
                if (nonMainImageIndex == 0) {
                    firstUploadedImage.setImage(ImageCache.getImage(image.getId()));
                    expand1.setOnAction(e -> previewImage(firstUploadedImage.getImage()));
                } else if (nonMainImageIndex == 1) {
                    secondUploadedImage.setImage(ImageCache.getImage(image.getId()));
                    expand2.setOnAction(e -> previewImage(secondUploadedImage.getImage()));
                }
            }
        });
    }


    private void selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        // select only jpeg and png files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        // show open file dialog<<
        File selectedFile = fileChooser.showOpenDialog(getStage());
        if(selectedFile != null) {
            // set image fit to width and height
            selectedImage.setPreserveRatio(true);
            selectedImage.setFitHeight(600);
            selectedImage.setFitHeight(500);
            selectedImageFile = selectedFile;
            selectedImage.setImage(new javafx.scene.image.Image(selectedFile.toURI().toString()));
            changeSelectedFileHBox();
        }
    }

    private void changeSelectedFileHBox() {
        newFileUploadBox.setVisible(true);
        fileName.setText(selectedImageFile.getName());
        uploadBtn.setOnAction(e -> uploadFile());
        cancelBtn.setOnAction(e -> cancelUpload());
    }

    private void uploadFile() {
        // here reupload image file
        statusSpinner.setVisible(true);
        // find in current projects which is the main
        int mainImageId = currentProject.getProjectImages().stream().filter(CustomImage::isMainImage).findFirst().get().getId();

        Image isReuploaded = projectModel.reuploadImage(currentProject.getId(),mainImageId, selectedImageFile);
        if(isReuploaded != null) {
            mainImage = isReuploaded;
            AlertHelper.showDefaultAlert("Image uploaded ", Alert.AlertType.INFORMATION);
            statusSpinner.setVisible(false);
            return;
        }
        AlertHelper.showDefaultAlert("Image not uploaded", Alert.AlertType.ERROR);
        statusSpinner.setVisible(false);
        cancelUpload();
    }

    private void cancelUpload() {
        newFileUploadBox.setVisible(false);
        fileName.setText("");
        selectedImageFile = null;
        selectedImage.setImage(mainImage);
    }



    private void previewImage(Image image ) {
        // open new scene with image inside
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Authorize yourself");
        stage.initStyle(StageStyle.DECORATED);
        stage.setResizable(false);

        ImageView imageView = new ImageView(image);
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


}
